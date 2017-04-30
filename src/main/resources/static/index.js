$('#custom-search').keypress(function(event) {
    document.getElementById('search-query').style.display = 'none';
    document.getElementById('search').style.display ='block';
    document.getElementById('graph').style.display = 'inline';
    $('#real-search').val(event.key);
    $('#real-search').focus();
});

$('#real-search').keypress(function(event) {
    if (event.keyCode == 13){
        event.preventDefault();
        onSearch();
    }
})

$("#search_button").click(onSearch);

function onSearch(){
        document.getElementById('search-result-container').style.display = 'block';
        $('#query').text($('#real-search').val());
        $.get("/question/search/relevant/"+$('#real-search').val()+"/1").done(function(data) {
             json = JSON.parse(data);
             $.each(json, function(key,value) {
               document.getElementById('result'+key).style.display = 'block';
               $('#result_title'+key).text(value.title.replace(/&quot;/g, '"'));
               $('#result_tags'+key).text(value.tags)
               //alert(value.tags);
               //alert(value.questionId);
               //alert(value.creationDate);
               //alert(value.score);
               //alert(value.answerCount);
             });
        });
}

function chooseNodes() {
    $.get("/numberOfNodesToShow/"+$('#numOfNodes').val()+"/").done();
}

