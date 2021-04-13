const Requests = function () {
    const POST = "POST";
    const GET = "GET";

    function ajax(type, url, params, isJsonResponse, options) {
        isJsonResponse = typeof isJsonResponse !== 'undefined' ? isJsonResponse : false;

        let xhr = new XMLHttpRequest();
        xhr.open(type, url, true);

        if (type !== GET) {
            xhr.setRequestHeader('Content-type', 'application/json');
        }
        xhr.onreadystatechange = function () {
            if (this.readyState === XMLHttpRequest.DONE) {
                if (this.status >= 200 && this.status < 400) {
                    if (options.success) {
                        let responseResult;
                        try {
                            responseResult = (isJsonResponse) ? JSON.parse(this.responseText) : this.responseText;
                        } catch (ex) {
                            console.error(ex);
                            responseResult = {};
                        }
                        options.success(responseResult);
                    }
                } else {
                    options.error && options.error(this.status);
                }
            }
        };

        console.log(params);
        xhr.send(params);
        return xhr;
    }

    function get(url, params, isJsonResponse, options) {
        return ajax(GET, url, params, isJsonResponse, options);
    }

    function post(url, params, isJsonResponse, options, type) {
        return ajax(type, url, params, isJsonResponse, options);
    }

    function encodeParams(params) {
        const urlParams = new URLSearchParams(Object.entries(params));
        return urlParams.toString();
    }

    function liftNamespace(params, portletNamespace) {
        for (let attr in params) {
            if (params.hasOwnProperty(attr)) {
                params[portletNamespace + attr] = params[attr];
            }
        }
        return params;
    }

    return {
        "ajax": ajax,
        "get": get,
        "post": post,
        "encodeParams": encodeParams,
        "liftNamespace": liftNamespace
    }
}();