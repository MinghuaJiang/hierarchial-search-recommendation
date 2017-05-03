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
         $.get("/recommendation/"+ d.name +"/" + count).done(function (obj) {
             $('node').hide("slow");
             $('link').hide("slow");

             var recommendWindow = d3.select("svg")
                 .selectAll("g")
                 .data(obj)
                 .enter()
                 .append("g")
                 .attr("class", "recommendPage");

             var tag = recommendWindow
                 .append("circle")
                 .attr("r", 15)
                 .attr("fill", "#ffccff");

             var tagName = recommendWindow
                 .append("text")
                 .attr("dx", -15)
                 .attr("dy", ".35em")
                 .text(function (obj) {
                     return obj.name;
                 });

             var recommend = recommendWindow
                 .selectAll("text")
                 .data(obj.recommend)
                 .enter()
                 .append("text")
                 .attr("class", "textBox")
                 .attr("border-radius", "10px")
                 .attr("border","2px solid #73AD21")
                 .attr("width", function () {
                     return this.getComputedTextLength() + "px";
                 })
                 .attr("height", "25px")
                 .attr("text-overflow", "inherit")
                 .attr("overflow","hidden");

             $('div .chip').ready(function(){
                 $('div .chip').jqFloat();
             });

             var force = d3.forceSimulation()
                 .force("link", d3.forceLink())
                 .force("charge", d3.forceManyBody())
                 .force("center", d3.forceCenter(width/2, height/2));

             force.nodes(obj.recommend).on({
                 click: function () {
                     function openPage() {
                         $.get("/openPage/"+$(this).val()+"/").done();
                     }
                 },
                 tick: ticked
                 });
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


