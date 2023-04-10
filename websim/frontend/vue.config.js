
module.exports = {
    productionSourceMap: true,
    assetsDir: 'static',

    devServer: {
        proxy: "http://pydev:8080"
    }
};