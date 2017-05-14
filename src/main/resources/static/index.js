$('#custom-search').keypress(function(event) {
    document.getElementById('search-query').style.display = 'none';
    document.getElementById('search').style.display ='block';
    document.getElementById('graphBox').style.display = 'inline';
    $('#real-search').val(event.key);
    $('#real-search').focus();
});

$('#real-search').keypress(function(event) {
    if (event.keyCode == 13){
        event.preventDefault();
        onSearch();
    }
});

$("#search_button").click(onSearch);

function onSearch(){
        document.getElementById('search-result-container').style.display = 'block';
        $('#query').text($('#real-search').val());
        $.get("/question/search/relevant/"+$('#real-search').val()+"/1").done(function(data) {
              json = JSON.parse(data);
              if($('#paging').data("twbs-pagination")){
                    $('#paging').twbsPagination('destroy');
              }
              var total = json['totalCount'];
              $("#result-count").text(total);
              var pages = json['totalPage'];
              $('#paging').twbsPagination({
                      totalPages: pages,
                      visiblePages: 7,
                      next: 'Next',
                      prev: 'Prev',
                      onPageClick: function (event, page) {
                          $.get("/question/search/relevant/"+$('#real-search').val()+"/"+page).done(function(data) {
                                       json = JSON.parse(data);
                                       $.each(json['questions'], function(key,value) {
                                         document.getElementById('result'+key).style.display = 'block';
                                         $('#result_title'+key).text($('<div/>').html(value.questionTitle).text());
                                         //$('#result_title'+key).attr('href',value.link);
                                         $('#result_tag'+key).empty();
                                         $.each(value.tags, function(k,v){
                                                         $('#result_tag'+key).append("<a style='display: inline-block; margin: 0px 5px 0px 0px;' href='#'> \
                                                                                                                                      <h4 class='label label-primary ng-binding' style='font-size: 100%;'>"
                                                                                                                                      + v +
                                                                                                                          "</h4> \
                                                                                                                           </a>"
                                                                             )
                                                                             });

                                         $('#result_content'+key).text($(value.questionBody).text().substr(0, 250) + "...");
                                       });

                                       for(i = json['questions'].length; i < 5; i++) {
                                            document.getElementById('result'+i).style.display = 'none';
                                       }
                                  });
                      }
                  });
        });
}


