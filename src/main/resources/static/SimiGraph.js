/**
 * Created by VINCENTWEN on 4/29/17.
 */

// D3 labeled forced layout

var relationGraph = d3.select("#innerGraph"),
    width = +relationGraph.attr("width"),
    height = +relationGraph.attr("height");

var radius = d3.scaleSqrt()
    .domain([0,20000])
    .range([0,20]);

var count = 8;

var color = d3.scaleOrdinal(d3.schemeCategory20);

var link = relationGraph.append("g")
    .attr("class", "link")
    .attr("stroke","#9ecae1")
    .attr("stroke-width","1.5px")
    .selectAll("line");

var node = relationGraph.append("g")
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
        .attr("border", "2px solid red")
        .attr("fill", function(d) { return color(d.group); });

    console.log("before dblclick: " + node);

    node.on("dblclick", function () {
        showRecommendGraph();
    });

    // node.on("click", function (d) {
    //
    //     $('#numOfNodes li').on('click', function(){
    //         count = $(this).text();
    //     });
    //     console.log(count);
    //
    //     $.get("/recommendation/question/"+ d.name +"/" + count).done(function (obj) {
    //      node.attr("visibility","hidden");
    //      link.attr("visibility","hidden");
    //      console.log("print d" + d);
    //      var json = JSON.parse(obj);
    //
    //      var recommendWindow = relationGraph
    //          .append("g")
    //          .attr("class", "recommendPage");
    //
    //      var centralTag = recommendWindow
    //          .append("g")
    //          .attr("class", "centralTag")
    //          .attr("transform", "translate(250,300)");
    //
    //      var tag = centralTag
    //          .append("circle")
    //          .attr("r", 100)
    //          .attr("fill", "#ffccff");
    //
    //      var tagName = centralTag
    //          .append("text")
    //          .attr("dx", -30)
    //          .attr("dy", ".35em")
    //          .attr("font-size", "20px")
    //          .attr("font-color", "white")
    //          .text(function () {
    //              return d.name;
    //          });
    //
    //      console.log("checking format:" + json);
    //
    //      var recommend = recommendWindow
    //          .selectAll("text")
    //          .data(json)
    //          .enter()
    //          .append("text")
    //          .attr("class", "textBox")
    //          .attr("border-radius", "10px")
    //          .attr("border","2px solid #73AD21")
    //          .attr("width", "100px")
    //          .attr("height", "25px")
    //          .attr("x", function () {
    //              return Math.random() * (width - 50);
    //          })
    //          .attr("y", function () {
    //              return Math.random() * (height - 50);
    //          })
    //          .attr("text-overflow", "inherit")
    //          .attr("overflow","hidden")
    //          .text(function (x) {
    //              console.log(x.questionTitle);
    //              return x["questionTitle"];
    //          });
    //
    //      // $(document).ready(function(){
    //      //     $('.textBox').jqFloat();
    //      // });
    //
    //      var force = d3.forceSimulation()
    //          .force("link", d3.forceLink())
    //          .force("charge", d3.forceManyBody())
    //          .force("center", d3.forceCenter(width/2, height/2));
    //
    //      force.nodes(recommend)
    //          .on("tick", ticked);
    //     });
    // });

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

$('#goBack').click(function () {
    $('.recommendPage').remove();
    node.attr("visibility","visible");
    link.attr("visibility","visible");
    console.log(node);
    $('#barChart').remove();
});




// recommendGraph
var margin = {top: 40, right: 20, bottom: 40, left: 45},
    barCharWidth = width - margin.left - margin.right,
    barCharHeight = height - margin.top - margin.bottom;

// var formatPercent = d3.format(".0%");

var x = d3.scaleBand()
    .range([0, barCharWidth])
    .padding(0.1);

var y = d3.scaleLinear()
    .range([height, 0]);

var barChart = d3.select("#innerGraph").append("svg")
    .attr("id", "barChart")
    .attr("width", barCharWidth + margin.left + margin.right)
    .attr("height", barCharHeight + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

var tip = d3.tip()
    .attr('class', 'd3-tip')
    .offset([-10, 0])
    .html(function(d) {
        return "<strong>questionTitle:</strong> <span style='color:red'>" + d.questionTitle + "</span>";
    });

barChart.call(tip);

function showRecommendGraph() {
    $.get("/recommendation/question/"+ node.name +"/" + count).done(function (obj) {
        var json = JSON.parse(obj);
        var arr = Object.values(json);
        console.log("arr:  " + arr);
        node.attr("visibility","hidden");
        link.attr("visibility","hidden");

        json.forEach(function (d) {
            d.horizontal = d.vote;
            d.vertical = d.answerCount;
        });

        x.domain(json.map(function(d) { return d.horizontal; }));
        y.domain([0, d3.max(json, function(d) { return d.vertical; })]);


        var tooltip = d3.select("body").append("div").attr("class", "toolTip");
        // append the rectangles for the bar chart
        barChart.selectAll(".bar")
            .data(json)
            .enter().append("rect")
            .attr("class", "bar")
            .attr("x", function(d) { return x(d.horizontal); })
            .attr("width", x.bandwidth())
            .attr("y", function(d) { return y(d.vertical); })
            .attr("height", function(d) {
                console.log(d.vertical);
                console.log(y(d.vertical));
                return barCharHeight - y(d.vertical);
            })
            .on("mousemove", function(d){
                tooltip
                    .style("left", d3.event.pageX - 50 + "px")
                    .style("top", d3.event.pageY - 70 + "px")
                    .style("display", "inline-block")
                    .html((d.questionTitle) + "<br>");
            })
            .on("mouseout", function(d){ tooltip.style("display", "none");});

        // add the x Axis
        barChart.append("g")
            .attr("transform", "translate(0," + barCharHeight + ")")
            // .append("text")
            // .attr("transform", "translate(" + (barCharWidth/2) + "," + (barCharHeight + margin.bottom - 5) + ")")
            // .text("vote")
            .call(d3.axisBottom(x));

        // add the y Axis
        barChart.append("g")
            // .append("text")
            // .attr("transform", "translate(-35," +  (barCharHeight+margin.bottom)/2 + ") rotate(-90)")
            // .text("answer count")
            .call(d3.axisLeft(y));

        //add labels
        barChart
            .append("text")
            .attr("transform", "translate(-35," +  (barCharHeight+margin.bottom)/2 + ") rotate(-90)")
            .text("# answer count");

        barChart
            .append("text")
            .attr("transform", "translate(" + (barCharWidth/2) + "," + (barCharHeight + margin.bottom - 5) + ")")
            .text("vote");
    });
}

//
// function type(d) {
//     d.frequency = +d.frequency;
//     return d;
// }
