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

    // right click to show bar chart of the selected node
    node.on("contextmenu", function (d) {
        showRecommendGraph(d);
    });

    // three event wish to bound to same node, not possible
    node.on("dblclick", function (d) {
        node.attr("visibility","hidden");
        link.attr("visibility","hidden");

        $.get("/recommendation/question/"+ d.name +"/" + count).done(function (obj){
            json = obj;
            var recommendWindow = relationGraph
                .append("g")
                .attr("class", "recommendPage");

            var centralTag = recommendWindow
                .append("g")
                .attr("class", "centralTag")
                .attr("transform", "translate(250,300)");

            var tag = centralTag
                .append("circle")
                .attr("r", 100)
                .attr("fill", "#FF66FF");

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
                .attr("width", "100px")
                .attr("height", "25px")
                .attr("x", function () {
                    return Math.random() * (width - 50);
                })
                .attr("y", function () {
                    return Math.random() * (height - 50);
                })
                .attr("text-overflow", "inherit")
                .attr("overflow","hidden")
                .text(function (x) {
                    console.log(x.questionTitle);
                    return x["questionTitle"];
                });

            var force = d3.forceSimulation()
                .force("link", d3.forceLink())
                .force("charge", d3.forceManyBody())
                .force("center", d3.forceCenter(width/2, height/2));

            force.nodes(recommend)
                .on("tick", ticked);
        });
    });

//    node.on("click", function (d) {
//        $('#numOfNodes li').on('click', function(){
//            count = $(this).text();
//        });
//        console.log(count);
//        $.get("/recommendation/question/"+ d.name +"/" + count).done(function (rec){
//            if (error) throw error;
//
//            node.attr("visibility","hidden");
//            link.attr("visibility","hidden");
//            var pack = d3.pack()
//                .size([400, 400])
//                .padding(10);
//
//            var root = d3.hierarchy(rec)
//                .sum(function(d) { return +d["answerCount"]; })
//                .sort(function(a,b) {return d3.ascending(a.value,b.value)});
//
//            var descendants = pack(root);
//
//
//            var snail = d3.select("#innerGraph");
//
//            var descendant = snail.selectAll(".snail")
//                .data(descendants.children)
//                .enter().append("g")
//                .attr("class", "snail")
//                .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; })
//                .on("mouseover", showhide)
//                .on("mouseout", showhide);
//
//            descendant.append("circle")
//                .attr("r",function(d) { return d.r; })
//                .attr("class", function(d) {return d.data["tags"]});
//
//            descendant.append("text")
//                .text(function(d) { return d.data["questionTitle"];});
//        });
//    });

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

var x = d3.scaleBand()
    .range([0, barCharWidth])
    .padding(0.1);

var y = d3.scaleLinear()
    .range([height, 0]);

function showRecommendGraph(eventOwner) {
    var barChart = d3.select("#innerGraph").append("svg")
        .attr("id", "barChart")
        .attr("width", barCharWidth + margin.left + margin.right)
        .attr("height", barCharHeight + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    $.get("/recommendation/question/"+ eventOwner.name +"/" + count).done(function (obj) {
        var barData = JSON.parse(obj);
        console.log("barData: " + barData);
        // var arr = Object.values(barData);
        node.attr("visibility","hidden");
        link.attr("visibility","hidden");

        barData.forEach(function (d) {
            d.horizontal = d.questionTitle;
            d.vertical = d.answerCount;
        });

        x.domain(barData.map(function(d) { return barData.indexOf(d); }));
        y.domain([0, d3.max(barData, function(d) { return d.vertical; })]);


        var tooltip = d3.select("body").append("div").attr("class", "toolTip");
        // append the rectangles for the bar chart
        barChart.selectAll(".bar")
            .data(barData)
            .enter().append("rect")
            .attr("class", "bar")
            .attr("x", function(d) { return barData.indexOf(d) * x.bandwidth(); })
            .attr("width", x.bandwidth() - 5)
            .attr("y", function(d) { return y(d.vertical); })
            .attr("height", function(d) {
                console.log(d.vertical);
                console.log(y(d.vertical));
                return barCharHeight - y(d.vertical);
            })
            .on("mousemove", function(d){
                tooltip
                    .style("left", d3.event.pageX - 30 + "px")
                    .style("top", d3.event.pageY - 70 + "px")
                    .style("display", "inline-block")
                    .html((d.questionTitle) + "<br>");
            })
            .on("mouseout", function(d){ tooltip.style("display", "none");});

        // add the x Axis
        barChart.append("g")
            .attr("transform", "translate(0," + barCharHeight + ")")
            .call(d3.axisBottom(x));

        // add the y Axis
        barChart.append("g")
            .call(d3.axisLeft(y));

        //add labels
        barChart
            .append("text")
            .attr("transform", "translate(-35," +  (barCharHeight+margin.bottom)/2 + ") rotate(-90)")
            .text("Answer count");

        barChart
            .append("text")
            .attr("transform", "translate(" + (barCharWidth/2) + "," + (barCharHeight + margin.bottom - 5) + ")")
            // .attr("transform", "translate(" + 10 + "," + (barCharHeight + margin.bottom - 5) + ")")
            .text("Index of related questions");
    });
}


$(".label label-primary ng-bindin").click(function () {
    var barChart = d3.select("#innerGraph").append("svg")
        .attr("id", "barChart")
        .attr("width", barCharWidth + margin.left + margin.right)
        .attr("height", barCharHeight + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    $.get("/recommendation/question/"+ $(".label label-primary ng-bindin").innerHTML +"/" + count).done(function (obj) {
        var barData = JSON.parse(obj);
        console.log("barData: " + barData);
        // var arr = Object.values(barData);
        node.attr("visibility","hidden");
        link.attr("visibility","hidden");

        barData.forEach(function (d) {
            d.horizontal = d.questionTitle;
            d.vertical = d.answerCount;
        });

        x.domain(barData.map(function(d) { return barData.indexOf(d); }));
        y.domain([0, d3.max(barData, function(d) { return d.vertical; })]);


        var tooltip = d3.select("body").append("div").attr("class", "toolTip");
        // append the rectangles for the bar chart
        barChart.selectAll(".bar")
            .data(barData)
            .enter().append("rect")
            .attr("class", "bar")
            .attr("x", function(d) { return barData.indexOf(d) * x.bandwidth(); })
            .attr("width", x.bandwidth() - 5)
            .attr("y", function(d) { return y(d.vertical); })
            .attr("height", function(d) {
                console.log(d.vertical);
                console.log(y(d.vertical));
                return barCharHeight - y(d.vertical);
            })
            .on("mousemove", function(d){
                tooltip
                    .style("left", d3.event.pageX - 30 + "px")
                    .style("top", d3.event.pageY - 70 + "px")
                    .style("display", "inline-block")
                    .html((d.questionTitle) + "<br>");
            })
            .on("mouseout", function(d){ tooltip.style("display", "none");});

        // add the x Axis
        barChart.append("g")
            .attr("transform", "translate(0," + barCharHeight + ")")
            .call(d3.axisBottom(x));

        // add the y Axis
        barChart.append("g")
            .call(d3.axisLeft(y));

        //add labels
        barChart
            .append("text")
            .attr("transform", "translate(-35," +  (barCharHeight+margin.bottom)/2 + ") rotate(-90)")
            .text("Answer count");

        barChart
            .append("text")
            .attr("transform", "translate(" + (barCharWidth/2) + "," + (barCharHeight + margin.bottom - 5) + ")")
            // .attr("transform", "translate(" + 10 + "," + (barCharHeight + margin.bottom - 5) + ")")
            .text("Index of related questions");
    });
});

function showhide(d,i) {
    var thisObject = d3.select(this).selectAll("text");
    if(thisObject.style("visibility") == "hidden"){
        thisObject.style("visibility", "visible");
    }else{
        thisObject.style("visibility", "");
    }
}
