$('document').ready(function(){

    totalPriceCalculation();

    $('input[type=number]').change(function(){

        var quantity = $(this).val();
        var price = $(this).data("price");
        $(this).parent().next().text(price * quantity);
        var totalPrice = 0;
        $('.totalPrice').each(function(){
            totalPrice += parseInt($(this).text())
        });
        $('#totalPrice').text(totalPrice);
    });

    $(".deleteButton").click(function(){
        var id = $(this).data("id");
        var target = $(this);
        $.ajax({
                   method: 'POST',
                   url: '/delete/' + id,
                   data: JSON.stringify(id),
                   contentType: 'application/json;charset=UTF-8',
                   success: function(result) {
                       console.log('ajax post successful');
                       target.parents().eq(1).remove();
                       totalPriceCalculation();
                   },
                   error: function() {
                       console.log('ajax post failed');
                       window.location.href = "/fail"
                   }

               });

    });

    $("#submitButton").click(function(){
        if ($("tr").length > 2) {
            $("form").submit();
        } else if ($("#warning").length == 0) {
            $(".container").prepend("<p class='warning' id='warning'><strong>There is no item in your cart!</strong></p>");
        };
    })

});

function totalPriceCalculation(){
    var totalPrice = 0;
        $('.totalPrice').each(function(){
                    totalPrice += parseInt($(this).text())
                });
        $('#totalPrice').text(totalPrice);
}