$('document').ready(function () {

    $('.addButton').click(function () {
        var id = $(this).data("id");
        console.log(id);
        $.ajax({
            method: 'POST',
            url: '/add-to-cart/' + id,
            data: JSON.stringify(id),
            contentType: 'application/json;charset=UTF-8',
            success: function (result) {
                console.log('ajax post successful');
                $(".cart").text(result);
                $(".alert").removeClass("in").show();
                $(".alert").delay(1000).addClass("in").fadeOut(2000);
            },
            error: function () {
                console.log('ajax post failed');
                window.location.href = "/fail";
            }

        });
    });

    $("#signUpButton").click(function () {
        $("#registerWarning").text("");
        userName = $("#userName").val();
        $("#register").find($("input")).each(function () {
            if ($(this).val() == "") {
                $("#registerWarning").text("Fill out all the fields please!");
                return;
            }
            ;
        });
        if($("#registerWarning").text() != ""){
                    return;
                }
        if($("#register").find($("input[type=email]")).val().indexOf("@") == -1){
                        $("#registerWarning").text("Not a valid email address!");
                        return;
                    }
        if($("#registerWarning").text() != ""){
            return;
        }
        $.ajax({
            method: 'POST',
            url: '/registerCheck/' + userName,
            data: JSON.stringify(userName),
            contentType: 'application/json;charset=UTF-8',
            success: function (result) {
                console.log('ajax post successful');
                if (result == 'true') {
                    if ($("#password").val() == $("#password2").val()) {
                        $("#registerForm").submit();
                    } else {
                        $("#registerWarning").text("Passwords do not match!");
                    }
                } else {
                    $("#registerWarning").text("Username already taken!")
                }
                ;
            },
            error: function () {
                console.log('ajax post failed');
            }

        });
    })

    $("#loginButton").click(function () {
        $("#loginWarning").text("");
        userName = $("#loginUserName").val();
        password = $("#loginPassword").val();
        $("#login").find($("input")).each(function () {
            if ($(this).val() == "") {
                $("#loginWarning").text("Fill out all the fields please!");
                return;
            }
            ;
        });
        if ($("#loginWarning").text() != "") {
            return;
        }
        $.ajax({
            method: 'POST',
            url: '/loginCheck/' + userName + '/' + password,
            data: JSON.stringify(userName),
            contentType: 'application/json;charset=UTF-8',
            success: function (result) {
                console.log('ajax post successful');
                console.log(typeof result);
                if (result == 'true') {
                    $("#loginForm").submit();
                } else {
                    $("#loginWarning").text("Username or password is incorrect!")
                }
                ;
            },
            error: function () {
                console.log('ajax post failed');
            }

        });
    })

});