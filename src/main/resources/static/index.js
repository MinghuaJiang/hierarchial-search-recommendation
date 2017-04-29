$('#custom-search').keypress(function(event) {
    document.getElementById('search-query').style.display = 'none';
    document.getElementById('search').style.display ='block';

    $('#real-search').val(event.key);
    $('#real-search').focus();
});

$('#real-search').keypress(function(event) {
    if (event.keyCode == 13){
        event.preventDefault();
        document.getElementById('search-result-container').style.display = 'block';
        $('#query').text($('#real-search').val())
    }
})