import * as pc from 'playcanvas'
import * as pcx from './extras/index.js'
import earcut from 'earcut';
import * as pg_helper from './polygon_helper.js'
import createFlyCameraScript from './fly-camera.js';

let app = null; // pc.Application
let bulbs = []; // map of bulb ID to bulb node
let bulbInfo = [];
let active_frame_data = new Array(512).fill(0.5);
let active_frame_source = null; // The frame source (IE PGM player)
let camera = null;

export function init(canvas) {
    app = new pc.Application(canvas, {
        mouse: new pc.Mouse(canvas),
        keyboard: new pc.Keyboard(window)
    });
    globalThis.pc = pc; // export pc such that scripts work. Not sure if this is the best way

    // fill the available space at full resolution
    app.setCanvasFillMode(pc.FILLMODE_NONE);
    app.setCanvasResolution(pc.RESOLUTION_AUTO);

    let miniStats = new pcx.MiniStats(app);

    // ensure canvas is resized when window changes size
    window.addEventListener('resize', () => app.resizeCanvas());

    const gesims = new pc.Entity("gesims");
    app.root.addChild(gesims);
    gesims.translate(0, 0, 0);

    // create camera entity
    camera = new pc.Entity('camera');
    camera.addComponent('camera', {
        clearColor: new pc.Color(0, 0, 0)
    });
    app.root.addChild(camera);
    
    // add the fly camera script to the camera
    camera.addComponent("script");
    camera.script.create(createFlyCameraScript());

    resetCameraPosition();
    // app.assets.loadFromUrl('static/js/fly-camera.js', 'script', function (err, asset) {
    //     camera.addComponent("script");
    //     camera.script.create("flyCamera");
    // });


    // create directional light entity
    const light = new pc.Entity('globalLight');
    light.addComponent('light', {
        intensity: 0.3,
    });
    app.root.addChild(light);
    light.setEulerAngles(90+30, 0, 0); // global illumination 30 degrees from below (street lights?)

    app.on('update', dt => {
        for (const bulb of bulbs) {
            // channels are 1 indexed, but data is zero indexed
            const r = active_frame_data[bulb.channels[0]-1]; 
            const g = active_frame_data[bulb.channels[1]-1];
            const b = active_frame_data[bulb.channels[2]-1];
            if (r === undefined || g === undefined || b === undefined) {
                alert("could not find frame data for bulb");
            }
            // TODO improve the algorithm below
            // We do not want the bulbs to become completely black
            // as the bulbs themselves are white.
            bulb.model.material.color.set(0.2+r, 0.2+g, 0.2+b);
            bulb.model.material.update()
            bulb.light.color = new pc.Color(r, g, b);
        }
    });

    app.start();
}

export function reset() {
    // Recreate the scene root from the app
    const gesims = app.root.findByName("gesims");
    let scene_root = gesims.findByName("scene_root");
    if (scene_root !== undefined) {
        gesims.removeChild(scene_root);
    }
    scene_root = new pc.Entity("scene_root")
    gesims.addChild(scene_root);

    // Reset all the bulbs
    bulbs = [];
    // Reset the frame data to mid values
    active_frame_data = new Array(512).fill(0.5);
}

function create_plane_from_polygon(node, outline, holes) {
    // Flatten all the vertices
    // then run earcut to triangulate the polygon
    const vertices_2d = [];
    for (const p of outline) {
        vertices_2d.push(p[0], p[1])
    }
    const holes_index_ranges = [];
    for (const hole of holes) {
        const start = vertices_2d.length/2;
        for (const p of hole) {
            vertices_2d.push(p[0], p[1])
        }
        holes_index_ranges.push(start);
    }    
    
    const indices = earcut(vertices_2d, holes_index_ranges, 2);

    // Convert to 2d vertices (not sure if needed)
    const vertices = [];
    for (let i = 0; i <= vertices_2d.length; i+=2) {
        vertices.push(vertices_2d[i], vertices_2d[i+1], 0.0);
    }
    
    const normals = [];
    for (let i = 0; i <= vertices.length/3; i++) {
        normals.push(0.0, 0.0, 1.0);
    }

    const colors = [];
    for (let i = 0; i <= vertices.length/3; i++) {
        colors.push(1.0, 1.0, 1.0, 0.0); // rgba
    }

    // Create a new mesh
    const mesh = new pc.Mesh(app.graphicsDevice);
    mesh.setPositions(vertices);
    mesh.setIndices(indices);
    mesh.setNormals(normals);
    mesh.update();

    // Create the material
    // const material = new pc.BasicMaterial();
    const material = new pc.StandardMaterial();
    material.diffuse.set(1.0, 1.0, 1.0);
    material.update();
    
    return new pc.MeshInstance(mesh, material, node);
}

function create_alu_mesh_from_path(node, path, outside) {
    const height = 0.15;

    let perimeter = path.map(p => new pg_helper.Point2D(p[0], p[1]))
    perimeter = pg_helper.orderClockwise(perimeter);

    const lines_2d = pg_helper.pathToLines(perimeter);
    const normals_2d = pg_helper.linesToNormals(lines_2d, outside);

    const vertices = [];
    const normals = [];
    const triangles = [];
    
    for (let i = 0; i < lines_2d.length; i++) {
        const l = lines_2d[i];
        const normal = normals_2d[i];


        /*
        Each line segment will be created using a single square face.
        The square is made out of two triangles and 4 vertices.
        All the vertices will share the same normal

        vertex names
        ab = a - bottom
        at = a - top
        bb = b - bottom
        bt = b - top

        ab -- at
        |    / |
        |   /  |
        | /    |
        bb -- bt
        */
        
        const index_ab = vertices.length/3;
        const index_at = index_ab+1;
        const index_bb = index_at+1;
        const index_bt = index_bb+1;

        vertices.push(l.a.x, l.a.y, 0.0); // ab
        vertices.push(l.a.x, l.a.y, height); // at
        vertices.push(l.b.x, l.b.y, 0.0); // bb
        vertices.push(l.b.x, l.b.y, height); // bt

        normals.push(normal.x, normal.y, 0.0); 
        normals.push(normal.x, normal.y, 0.0); 
        normals.push(normal.x, normal.y, 0.0); 
        normals.push(normal.x, normal.y, 0.0); 

        if (outside) {
            // triangle ab - at - bb
            triangles.push(index_ab, index_at, index_bb);
            // triangle bb - at - bt
            triangles.push(index_bb, index_at, index_bt);
        } else {
            // triangle bb - at - ab
            triangles.push(index_bb, index_at, index_ab);
            // triangle bt - at - bb
            triangles.push(index_bt, index_at, index_bb);
        }        
    }

    const mesh = new pc.Mesh(app.graphicsDevice);
    mesh.setPositions(vertices);
    mesh.setIndices(triangles);
    mesh.setNormals(normals);
    mesh.update();
    

    const material = new pc.StandardMaterial();
    material.diffuse.set(0.972, 0.960, 0.915) 
    material.emissive.set(0.1, 0.1, 0.1);
    material.metalness = 1
    material.update();
    
    return new pc.MeshInstance(mesh, material, node);
}

export function loadSign(api_base) {
    // Reset the scene first
    reset();

    const scene_root = app.root.findByName("scene_root");

    
    let promise = 
    fetch(api_base+"/scene.json")
        .then(response => response.json())
        .then(data => {
            // create box entity
            if (data.background !== undefined) {
                const sign_bg = new pc.Entity('sign_bg');
                sign_bg.addComponent('model', {
                    type: 'plane'
                });
                sign_bg.rotate(90, 0, 0);
                sign_bg.setLocalScale(data.background.size[0], 1, data.background.size[1]);
                sign_bg.translate(0, data.background.size[1]/2, 0);
                scene_root.addChild(sign_bg);
                

                app.loader.load(api_base+"/assets/"+data.background.texture, "texture", function(err, texture){
                    let material = new pc.StandardMaterial();
                    material.diffuseMap = texture;
                    material.opacityMap = texture;
                    material.blendType = pc.BLEND_NORMAL;
                    material.update();
            
                    sign_bg.model.material = material;
                });
            
            }
            
            const camera = app.root.findByName("camera");
            const globalLight = app.root.findByName("globalLight");
            bulbInfo = []
            for (const [group_index, j_group] of data['groups'].entries()) {
                const group = new pc.Entity("group_"+group_index);
                group.translate(j_group.pos[0], j_group.pos[1], 0.01);

                const group_layer = new pc.Layer({name: "group_"+group_index})
                app.scene.layers.pushOpaque(group_layer);
                camera.camera.layers = camera.camera.layers.concat([group_layer.id]);
                // camera.camera.update();

                // Handle aluminium if any
                if (j_group.alu !== undefined) {
                    const j_alu = j_group.alu;

                    // Create the meshes for alu and back plate
                    // We will create two models
                    // - one for the back plate and inside facing aluminium (will receive lightt)
                    // - one for the outside facing aluminium (will only receive global light)

                    {
                        // Inside alu and plane
                        const node = new pc.GraphNode();
                        const meshes = [create_plane_from_polygon(node, j_alu.outline, j_alu.holes)];
                        meshes.push(create_alu_mesh_from_path(node, j_alu.outline, false));
                        for (const hole of j_alu.holes) {
                            meshes.push(create_alu_mesh_from_path(node, hole, true));
                        }

                        // Create a model and add the mesh instance to it
                        var model = new pc.Model();
                        model.graph = node;
                        model.meshInstances = meshes;

                        // Create the entity
                        const alu_entity = new pc.Entity("alu_"+group_index+"_inside");
                        alu_entity.addComponent('model', {
                            type: 'asset',
                            layers: [group_layer.id]
                        });
                        alu_entity.model.model = model;

                        group.addChild(alu_entity);
                    } 
                    {
                        // Outside facing alu
                        const node = new pc.GraphNode();
                        const meshes = [];
                        meshes.push(create_alu_mesh_from_path(node, j_alu.outline, true));
                        for (const hole of j_alu.holes) {
                            meshes.push(create_alu_mesh_from_path(node, hole, false));
                        }

                        // Create a model and add the mesh instance to it
                        var model = new pc.Model();
                        model.graph = node;
                        model.meshInstances = meshes;

                        // Create the entity
                        const alu_entity = new pc.Entity("alu_"+group_index+"_outside");
                        alu_entity.addComponent('model', {
                            type: 'asset',
                        });
                        alu_entity.model.model = model;

                        group.addChild(alu_entity);
                    }
                    
                }

                // Handle bulbs
                for(const j_bulb of j_group["bulbs"]) {
                    const bulb_id = j_bulb[0];
                    const bulb_pos = [j_bulb[1], j_bulb[2]];
                    const bulb_channels = j_bulb.slice(3);
                    const bulb = new pc.Entity("bulb_"+bulb_id);
                    group.addChild(bulb);
                    bulbs.push(bulb);
                    bulbInfo.push({
                        id: bulb_id,
                        channels: bulb_channels,
                    });

                    bulb.addComponent('model', {
                        type: 'sphere',
                    });
                    bulb.setLocalScale(0.05, 0.05, 0.05); // scale to 5cm diameter
                    bulb.translate(bulb_pos[0], bulb_pos[1], 0.07);

                    bulb.model.material = new pc.BasicMaterial();
                    bulb.model.material.color.set(0.2, 0.2, 0.2);
                    bulb.model.material.update();

                    bulb.addComponent('light', {
                        type: "point",
                        color: new pc.Color(0.1, 0.1, 0.1),
                        intensity: 1.0,
                        range: 0.4,
                        layers: [group_layer.id]
                    });
                    globalLight.light.layers = globalLight.light.layers.concat(group_layer.id)

                    bulb.channels = bulb_channels;
                }

                scene_root.addChild(group);
            }

            bulbInfo.sort((a, b) => a.id - b.id);
        });

        return promise;
}

export function startFrameSource(frame_source) {
    cancelActiveFrameSource()
    active_frame_source = frame_source;
    active_frame_source.start();
}

export function cancelActiveFrameSource() {
    if(active_frame_source !== null) {
        active_frame_source.cancel();
        active_frame_source = null;
    }
}

export function pushFrame(frame_data) {
    active_frame_data = frame_data;
}

export function getBulbs() {
    return bulbInfo;
}

export function resetCameraPosition() {
    camera.script["flyCamera"].resetCamera();
}