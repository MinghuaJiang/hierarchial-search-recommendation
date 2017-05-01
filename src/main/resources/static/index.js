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
        $.get("/question/search/count/"+$('#real-search').val()).done(function(data) {
              json = JSON.parse(data);
              var total = json['total'] - 1
              $("#result-count").text(total);
              var pages = (total - 1) / 5 + 1;
              if($('#paging').data("twbs-pagination")){
                    $('#paging').twbsPagination('destroy');
              }
              $('#paging').twbsPagination({
                      totalPages: pages,
                      visiblePages: 7,
                      next: 'Next',
                      prev: 'Prev',
                      onPageClick: function (event, page) {
                          $.get("/question/search/relevant/"+$('#real-search').val()+"/"+page).done(function(data) {
                                       json = JSON.parse(data);
                                       $.each(json['items'], function(key,value) {
                                         document.getElementById('result'+key).style.display = 'block';
                                         $('#result_title'+key).text($('<div/>').html(value.title).text());
                                         $('#result_title'+key).attr('href',value.link);
                                         $('#result_tag'+key).empty();
                                         $.each(value.tags, function(k,v){
                                                         $('#result_tag'+key).append("<a style='display: inline-block; margin: 0px 5px 0px 0px;' href='#'> \
                                                                                                                                      <h4 class='label label-primary ng-binding' style='font-size: 100%;'>"
                                                                                                                                      + v +
                                                                                                                          "</h4> \
                                                                                                                           </a>"
                                                                             )
                                                                             });

                                         $('#result_content'+key).text($(value.body).text().substr(0, 250) + "...");
                                         //alert(value.tags);
                                         //alert(value.questionId);
                                         //alert(value.creationDate);
                                         //alert(value.score);
                                         //alert(value.answerCount);
                                       });
                                  });
                      }
                  });
        });
}

function chooseNodes() {
    $.get("/numberOfNodesToShow/"+$('#numOfNodes').val()+"/").done();
}

