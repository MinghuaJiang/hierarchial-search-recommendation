/**
 * Created by VINCENTWEN on 4/29/17.
 */

var svg = d3.select("#graph"),
    width = +svg.attr("width"),
    height = +svg.attr("height");

var radius = d3.scaleSqrt()
    .domain([0,20000])
    .range([0,20]);

var color = d3.scaleOrdinal(d3.schemeCategory20);

var link = svg.append("g")
    .attr("class", "link")
    .selectAll("line");

var drag = d3.drag()
    .on("start", dragstarted)
    .on("drag", dragged)
    .on("end", dragended);

var simulation = d3.forceSimulation()
    .force("link", d3.forceLink())
    .force("charge", d3.forceManyBody())
    .force("center", d3.forceCenter(width/2, height/2));

d3.json("../static/graph.json", function(error, json) {
    if (error) throw error;

    link = link.data(json.links).enter().append("line");

    var node = svg.selectAll(".node")
        .data(json.nodes)
        .enter().append("g")
        .attr("class", "node")
        .call(drag);

    // node.append("circle")
    //     .attr("r", 5)
    //     .attr("fill", function(d) { return color(d.group); });

    node.append("image")
        .attr("xlink:href", "https://github.com/favicon.ico")
        .attr("x", -15)
        .attr("y", -15)
        .attr("width", 30)
        .attr("height", 30);

    node.append("text")
        .attr("dx", 20)
        .attr("dy", ".35em")
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
