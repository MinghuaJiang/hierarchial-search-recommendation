/**
 * Created by VINCENTWEN on 4/29/17.
 */

// D3 labeled forced layout

var svg = d3.select("svg"),
    width = +svg.attr("width"),
    height = +svg.attr("height");

var radius = d3.scaleSqrt()
    .domain([0,20000])
    .range([0,20]);

var color = d3.scaleOrdinal(d3.schemeCategory20);

var link = svg.append("g")
    .attr("class", "link")
    .attr("stroke","#9ecae1")
    .attr("stroke-width","1.5px")
    .selectAll("line");

var node = svg.append("g")
    .attr("class", "nodes")
    .selectAll(".node");

var drag = d3.drag()
    .on("start", dragstarted)
    .on("drag", dragged)
    .on("end", dragended);

var simulation = d3.forceSimulation()
    .force("link", d3.forceLink())
    .force("charge", d3.forceManyBody())
    .force("center", d3.forceCenter(width/2, height/2));

d3.json("/graph.json", function(error, json) {
    if (error) throw error;

    link = link.data(json.links).enter().append("line");

    node = node.data(json.nodes)
        .enter().append("g")
        .attr("class","node")
        .call(drag);

    node.append("circle")
        .attr("r", 10)
        .attr("fill", function(d) { return color(d.group); });
    
    node.on("click", function (d) {
         var count = 8;
         $.get("/recommendation/question/"+ d.name +"/" + count).done(function (obj) {
             node.style("opacity", 0);
             link.style("opacity", 0);
             // console.log(d);
             // console.log(obj);
             // console.log(this);
             var json = JSON.parse(obj);
             console.log("#" + json.questions);
             console.log("&" + json["questions"]);
             console.log("*" + json['questions'])

             var recommendWindow = d3.select("svg")
                 .append("g")
                 .attr("class", "recommendPage");

             var centralTag = recommendWindow
                 .append("g")
                 .attr("class", "centralTag")
                 .attr("transform", "translate(200,300)");

             var tag = centralTag
                 .append("circle")
                 .attr("r", 100)
                 .attr("fill", "#ffccff");

             var tagName = centralTag
                 .append("text")
                 .attr("dx", -30)
                 .attr("dy", ".35em")
                 .attr("font-size", "20px")
                 .attr("font-color", "white")
                 .text(function () {
                     return d.name;
                 });

             var recommend = recommendWindow
                 .selectAll("text")
                 .data(json)
                 .enter()
                 .append("text")
                 .attr("class", "textBox")
                 .attr("border-radius", "10px")
                 .attr("border","2px solid #73AD21")
                 .attr("width", function (x) {
                     return x.questionTitle.getComputedTextLength + "px";
                 })
                 .attr("height", "25px")
                 .attr("text-overflow", "inherit")
                 .attr("overflow","hidden")
                 .text(function (x) {
                     // alert(x.questionTitle); not working
                     // alert(x["questionTitle"]); still not working
                     // alert(x.questionTitle); // work !!!!!
                     x.questionTitle;
                 });

             $(document).ready(function(){
                 $('.textBox').jqFloat();
             });

             var force = d3.forceSimulation()
                 .force("link", d3.forceLink())
                 .force("charge", d3.forceManyBody())
                 .force("center", d3.forceCenter(width/2, height/2));

             // force.nodes(recommend).on({
             //     click: function () {
             //         function openPage() {
             //             $.get("/openPage/"+$(this).val()+"/").done();
             //         }
             //     },
             //     tick: ticked
             //     });
             force.nodes(recommend)
                 .on("tick", ticked);
                 // .on("click", function () {
                 //     function openPage() {
                 //         $.get("/open")
                 //     }
                 // })
         });
    });

    // use same image on each node
    // node.append("image")
    //     .attr("xlink:href", "https://github.com/favicon.ico")
    //     .attr("x", -15)
    //     .attr("y", -15)
    //     .attr("width", 30)
    //     .attr("height", 30);

    node.append("text")
        .attr("dx", 20)
        .attr("dy", ".35em")
        .attr("font", "10px sans-serif")
        .text(function(d) { return d.name });

    simulation.nodes(json.nodes)
        .on("tick", ticked);
    simulation.force("link")
        .links(json.links);
    simulation.restart();


    function ticked() {
        link
            .attr("x1", function (d) {
                return d.source.x;
            })
            .attr("y1", function (d) {
                return d.source.y;
            })
            .attr("x2", function (d) {
                return d.target.x;
            })
            .attr("y2", function (d) {
                return d.target.y;
            });

        node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
    }
});

function dragstarted(d) {
    if (!d3.event.active) simulation.alphaTarget(0.3).restart();
    d.fx = d.x, d.fy = d.y;
}

function dragged(d) {
    d.fx = d3.event.x, d.fy = d3.event.y;
}

function dragended(d) {
    if (!d3.event.active) simulation.alphaTarget(0);
    d.fx = null, d.fy = null;
}

var innerGraph = $("#innerGraph");
console.log(innerGraph.width);

$('goBack').click(function () {
    $('recommenPage').remove();
    $('node').show("fast");
    $('link').show("fast");
});


