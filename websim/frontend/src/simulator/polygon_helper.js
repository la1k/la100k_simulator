
class Point2D {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
}

class Vector2D {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }

    get magnitude() {
        return Math.sqrt(this.x*this.x+this.y*this.y);
    }

    get normalized() {
        const magnitude = this.magnitude;
        return new Vector2D(this.x/magnitude, this.y/magnitude);
    }

    get inverted() {
        return new Vector2D(-this.x, -this.y);
    }
}

class Line2D {
    constructor(a, b) {
        this.a = a
        this.b = b
    }

    get delta() {
        return new Vector2D(this.b.x-this.a.x, this.b.y-this.a.y)
    }

    get normal() {
        const delta = this.delta
        return new Vector2D(delta.y, -delta.x);
    }
}


function orderClockwise(path) {
    // Path is an array of Point2D

    // Work on a copy of the path
    path = path.slice()

    // Check if the perimiter is in the clockwise order
    let sum_edges = 0;
    for (let i = 0; i < path.length; i++) {
        const cp = path[i];
        const np = path[(i+1)%path.length];
        sum_edges += (np.x-cp.x)*(np.y+cp.y);
    }

    // Reverse it if it is not in the clockwise order
    if (sum_edges < 0.0) {
        path = path.reverse();
    } 

    return path
}

function pathToLines(path) {
    let lines = [];
    for (let i = 0; i < path.length; i++) {
        const cp = path[i];
        const np = path[(i+1)%path.length];
        const line = new Line2D(cp, np);
        lines.push(line)
    }
    return lines;
}

function linesToNormals(lines, outside) {
    let normals = [];
    for (const l of lines) {
        const normal = l.normal.normalized
        if (outside) {
            normals.push(normal.inverted);
        }
        else {
            normals.push(normal)
        }
    }

    return normals;
}

export {Point2D, Vector2D, Line2D, orderClockwise, pathToLines, linesToNormals}