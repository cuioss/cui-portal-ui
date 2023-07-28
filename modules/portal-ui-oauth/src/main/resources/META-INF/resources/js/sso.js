/*function initTokenRenew() {
	if ($("form.cuiOauthForm").data("timeout") >= 0) {
		$("form.cuiOauthForm").data("timeoutCallback", setTimeout(function () {
			$.ajax({
						url: $("form.cuiOauthForm").data("uri"),
						xhrFields: {
							withCredentials: true
						}
					})
				.always(function () { reloadTokenForm(); })
			}, $("form.cuiOauthForm").data("timeout") * 1000));
	} else if ($("form.cuiOauthForm").data("timeout") < 0 && $("form.cuiOauthForm").data("uri")) {
		location.href = $("form.cuiOauthForm").data("uri");
	}
}*/

function initTokenRenew() {
    if ($("form.cuiOauthForm").data("timeout") >= 0) {
        $("form.cuiOauthForm").data("timeoutCallback", setTimeout(function () {
            $.ajax({
                url: $("form.cuiOauthForm").data("uri"),
                xhrFields: {
                    withCredentials: true
                },
                success: function (data) {
                    parseResponse(data);
                },
                error: function () {
                    location.href = $("form.cuiOauthForm").data("uri");
                }
            })

        }, $("form.cuiOauthForm").data("timeout") * 1000));
    } else if ($("form.cuiOauthForm").data("timeout") < 0 && $("form.cuiOauthForm").data("uri")) {
        location.href = $("form.cuiOauthForm").data("uri");
    }
}

function parseResponse(data) {
    var code = data.code;
    var state = data.state;
    if (null != code && null != state) {
        var loginUrl = $("form.cuiOauthForm").data("login")
        if (null != loginUrl) {
            var origin = window.location.origin;
            var path = window.location.pathname;
            var product = path.split("/")[1];
            var loginRedirect = origin+"/"+product+loginUrl+"?code=" + code + "&state=" + state;
            $.ajax({
                url: loginRedirect,
                xhrFields: {
                    withCredentials: true
                }
            })
                .always(function () { reloadTokenForm(); })
        }
    }
}

function stopRenew() {
    if ($("form.cuiOauthForm").data("timeoutCallback")) {
        clearTimeout($("form.cuiOauthForm").data("timeoutCallback"));
    }
}

$(function () {
    initTokenRenew();
    icw.cui.registerOnIdle(stopRenew)
});
