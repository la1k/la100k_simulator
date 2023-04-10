async function* makeTextFileLineIterator(fileURL) {
    const utf8Decoder = new TextDecoder("utf-8");
    let response = await fetch(fileURL);
    let reader = response.body.getReader();
    let {value: chunk, done: readerDone} = await reader.read();
    chunk = chunk ? utf8Decoder.decode(chunk, {stream: true}) : "";

    let re = /\r\n|\n|\r/gm;
    let startIndex = 0;
    let result;

    for (;;) {
        let result = re.exec(chunk);
        if (!result) {
            if (readerDone) {
                break;
            }
            let remainder = chunk.substr(startIndex);
            ({value: chunk, done: readerDone} = await reader.read());
            chunk = remainder + (chunk ? utf8Decoder.decode(chunk, {stream: true}) : "");
            startIndex = re.lastIndex = 0;
            continue;
        }
        yield chunk.substring(startIndex, result.index);
        startIndex = re.lastIndex;
    }
    if (startIndex < chunk.length) {
        // last line didn't end in a newline char
        yield chunk.substr(startIndex);
    }
}


export class PGMPlayer {
    constructor(url, frame_callback) {
        this.frame_callback = frame_callback;
        this.pgm_lines = makeTextFileLineIterator(url);
        this.cancelled = false;
        this.on_completed = null;
    }

    setOnCompleted(cb) {
        this.on_completed = cb;
    }

    async start() {
        const format_version = await this.pgm_lines.next();
        if (format_version.done || format_version.value != "P2") {
            alert("Invalid PGM format version definition");
            return;
        }
    
        const pgm_size = await this.pgm_lines.next();
        if (pgm_size.done) {
            alert("Invalid PGM size definition");
            return;
        }
        const [num_channels, num_frames] = pgm_size.value.split(" ").map(x => parseInt(x))
    
        const max_value_s = await this.pgm_lines.next();
        if (max_value_s.done) {
            alert("Invalid PGM max value definition");
            return;
        }
        const max_value = parseInt(max_value_s.value);
    
        for await (let line of this.pgm_lines) {
            if (this.cancelled) {
                break;
            }
            const frame_data = line.trim().split(" ").map(x => parseInt(x)/max_value);
            this.frame_callback(frame_data);
            await new Promise(r => setTimeout(r, 50)); // 20 FPS
        }

        if (this.on_completed) {
            this.on_completed();
        }
    }

    cancel() {
        this.cancelled = true;
    }
}