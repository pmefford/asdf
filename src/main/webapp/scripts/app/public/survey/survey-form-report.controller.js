'use strict';

angular.module('patientfocusApp')
    .controller('SurveyFormReportController', function ($scope, $timeout, $state, $window,
                                                        $stateParams, entity, PublicSurvey) {
        console.log('state params', $stateParams);
        $scope.report = $stateParams.entity;
        console.log('controller', $scope)
        $scope.charts = [];
        $scope.surveyContent = {};
        $scope.questionsMap = [];
        $scope.topcategory = -1;
        $scope.bottomcategory = -1;

        $scope.categories = [];

        $scope.load = function () {
            console.log($stateParams);

            //PublicSurvey.load({id: $stateParams.id},function(result){
            //    $scope.individualResponse=result;
            //});

            PublicSurvey.report({id: $stateParams.id}, function (result) {
                console.log('report:', result);
                $scope.report = result;
                $scope.loadSurveyFirst();
            });


        };

        $scope.loadSurveyFirst = function () {
            PublicSurvey.reportId({id: $stateParams.id}, function (result) {
                console.log('survey:', result);
                $scope.individualResponse = result;
                $scope.takenDate = new Date(result.updated);
                PublicSurvey.questions({id: $scope.individualResponse.accessKey}, function (content) {
                    $scope.surveyContent = content;
                    $scope.categories = $scope._setCategories(content);
                });
            });
        };
        $scope.buildCategoryChart = function () {
            var individualScore = [];
            var populationScore = [];
            var populationCatories = [];
            var individualCateories = [];
            var categoryNames = [];
            for (var i = 0; i < $scope.report.questions.length; i++) {
                var question = $scope.report.questions[i];
                var questionContent = $scope.questionsMap[question.key];

                var sum = 0;
                var answerCount = 0;
                for (var key in  question.answers) {
                    console.log(question.key + " sum:" + question.answers[key].sum + " count:" + question.answers[key].count);
                    console.log("agSum:" + sum + " agCount:" + answerCount);
                    sum += parseInt(question.answers[key].sum);
                    answerCount += parseInt(question.answers[key].count);
                }
                console.log("sum:" + sum);
                console.log("answerCount:" + answerCount);
                if (populationCatories[questionContent.category] != undefined) {
                    populationCatories[questionContent.category].sum += parseInt(sum);
                    populationCatories[questionContent.category].count += parseInt(answerCount);
                } else {
                    populationCatories[questionContent.category] = {
                        sum: parseInt(sum),
                        count: parseInt(answerCount),
                        name: questionContent.category
                    };
                    categoryNames.push(questionContent.category);
                }
            }
            for (i = 0; i < categoryNames.length; i++) {
                populationScore.push(Math.round((populationCatories[categoryNames[i]].sum / populationCatories[categoryNames[i]].count) * 100) / 100);
            }


            for (var i = 0; i < $scope.individualResponse.questionList.length; i++) {
                var questionResponse = $scope.individualResponse.questionList[i];
                var questionContent = $scope.questionsMap[questionResponse.questionKey];
                if (questionContent.type != 'range') continue;
                if (individualCateories[questionContent.category] != undefined) {

                    var answer = 0;
                    if (questionResponse.answers != null) {
                        answer = questionResponse.answers;
                    }
                    individualCateories[questionContent.category].sum += parseInt(answer);
                    individualCateories[questionContent.category].count += 1;
                } else {
                    var answer = 0;
                    if (questionResponse.answers != null) {
                        answer = questionResponse.answers;
                    }
                    individualCateories[questionContent.category] = {
                        sum: parseInt(answer),
                        count: 1,
                        name: questionContent.category
                    };
                }
            }
            categoryNames.sort();
            var topScore = -1;
            var bottomScore = -1;
            var catLabels = [''];
            for (i = 0; i < categoryNames.length; i++) {

                if (individualCateories[categoryNames[i]] != undefined) {
                    var score = Math.round((individualCateories[categoryNames[i]].sum / individualCateories[categoryNames[i]].count) * 100) / 100;
                    individualScore.push(score);

                    var differences = score - populationScore[i];
                    if ($scope.topcategory == -1 || differences >= topScore) {
                        $scope.topcategory = i + 1;
                        topScore = differences;
                    }
                    if ($scope.bottomcategory == -1 || ( differences <= bottomScore)) {
                        $scope.bottomcategory = i + 1;
                        bottomScore = differences;
                    }
                }
                console.log('width: ', $window.innerWidth);
                if ($window.innerWidth > 750) {
                    catLabels.push(categoryNames[i]);
                } else {
                    catLabels.push(categoryNames[i].charAt(0));
                }
            }
            categoryNames.splice(0, 0, '');
            categoryNames.push('');
            catLabels.push('');
            populationScore.splice(0, 0, 4);
            populationScore.push(4);
            individualScore.splice(0, 0, 4);
            individualScore.push(4);
            var ctx = document.getElementById("categoryChart").getContext("2d");
            var lineChartData = {
                labels: catLabels,
                datasets: [
                    {
                        label: "Your Responses",
                        fillColor: "rgba(205, 43, 49,0.2)",
                        strokeColor: "#CD2B31",
                        pointColor: "rgba(205, 43, 49,1)",
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: "rgba(205, 43, 49,1)",
                        data: individualScore
                    },
                    {
                        label: "Global Norm",
                        fillColor: "rgba(2,1,1,0.2)",
                        strokeColor: "#211",
                        pointColor: "rgba(2,1,1,1)",
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: "rgba(2,1,1,1)",
                        data: populationScore
                    }
                ]
            };
            var catChart = new Chart(ctx).Line(lineChartData, {
                responsive: true,
                legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%= datasets[i].strokeColor %>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"
            });
            document.getElementById("categoryLegend").innerHTML = catChart.generateLegend();
            document.getElementById("categoryLegend2").innerHTML = catChart.generateLegend();
        };

        $scope.buildCharts = function () {
            if ($scope.chartsBuilt == 1) {
                return;
            } else {
                $scope.chartsBuilt = 1;
            }
            $scope.buildCategoryChart();
            for (var i = 0; i < $scope.report.questions.length; i++) {
                var answer = '';
                for (var j = 0; j < $scope.individualResponse.questionList.length; j++) {
                    if ($scope.report.questions[i].key == $scope.individualResponse.questionList[j].questionKey) {
                        answer = $scope.individualResponse.questionList[j].answers;
                        console.log("answer:" + answer);
                        break;
                    }
                }
                var average = 0;
                var sum = 0;
                var answerCount = 0;
                for (var key in  $scope.report.questions[i].answers) {
                    sum += $scope.report.questions[i].answers[key].sum;
                    answerCount += $scope.report.questions[i].answers[key].count;
                }
                average = ((sum / answerCount) * 100) / 100;
                var barChartData = {
                    labels: [""],
                    datasets: [
                        {
                            label: "Your Response",
                            fillColor: "rgba(205, 43, 49,0.5)",
                            strokeColor: "rgba(205, 43, 49,0.8)",
                            highlightFill: "rgba(205, 43, 49,0.75)",
                            highlightStroke: "rgba(205, 43, 49,1)",
                            data: [answer]
                        },
                        {
                            label: "Global Norm",
                            fillColor: "rgba(2, 1, 1,0.5)",
                            strokeColor: "rgba(2, 1, 1,0.8)",
                            highlightFill: "rgba(2, 1, 1,0.75)",
                            highlightStroke: "rgba(2, 1, 1,1)",
                            data: [average]
                        }
                    ]
                };

                var ctx = document.getElementById($scope.report.questions[i].key).getContext("2d");

                $scope.charts.push(new Chart(ctx).HorizontalBar(barChartData, {
                    responsive: true,
                    barShowStroke: true,
                    barDatasetSpacing: 5,
                    scaleIntegersOnly: true,
                    scaleBeginAtZero: true,
                    showScale: true,
                    scaleOverride: true,
                    scaleSteps: 6,
                    scaleStartValue: 1,
                    scaleStepWidth: 1,
                    maintainAspectRatio: true
                }));
            }


        };

        $scope._setCategories = function (content) {
            var category = content.questions[0].category;
            var questionList = [];
            var categories = [{
                name: content.questions[0].category,
                questions: []
            }];
            var catindex = 0;
            for (var i = 0; i < content.questions.length; i++) {
                var question = content.questions[i];
                $scope.questionsMap[question.key] = question;
                if (question.type !== 'range') {
                    continue;
                }
                var questionTxt = content.questions[i].text[0].fullText;
                if (category != question.category) {
                    catindex++;
                    category = question.category;
                    categories.push({
                        name: question.category,
                        questions: []
                    });
                }
                if (questionTxt != '') {
                    categories[catindex].questions.push({
                        fullText: questionTxt,
                        category: question.category,
                        type: question.type,
                        options: question.options,
                        key: question.key
                    });
                }
            }
            for (var i = 0; i < categories.length; i++) {
                if (categories[i].questions.length == 0) {
                    categories.splice(i, 1);
                }
            }
            return categories;
        };
        $scope.categoriesDrawn = function () {
            $scope.categoriesReady = true;
        };


        $scope.questionsDrawn = function () {
            if ($scope.categoriesReady) {
                $scope.questionsReady = true;

            }
        };
    });
