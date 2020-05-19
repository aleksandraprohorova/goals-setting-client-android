<!DOCTYPE html>
<html>
  <head>
    <script src="https://cdn.anychart.com/releases/8.7.1/js/anychart-base.min.js?hcode=a0c21fc77e1449cc86299c5faa067dc4"></script>
    <script src="https://cdn.anychart.com/releases/8.7.1/js/anychart-gantt.min.js?hcode=a0c21fc77e1449cc86299c5faa067dc4" type="text/javascript"></script>
  </head>
  <body>
    <div id="container"></div>
    <script>

    function plot(data) {
                var treeData = anychart.data.tree(data, "as-tree");
                var chart = anychart.ganttProject();
                chart.data(treeData);
                chart.container("container");
                chart.draw();
                chart.fitAll();
    }
    </script>
  </body>
</html>