'use strict';

angular.module('patientfocusApp')
    .controller('SurveyFormController', function ($scope, $rootScope, $timeout, $state,
                                                  $stateParams, PublicSurvey, SurveyFirst, entity, Auth) {
        console.log('SurveyFormController');
        $scope.surveyContent = entity;
        $scope.survey = {questions: []};
        $scope.currentCategory = '';
        $scope.uid = $stateParams.uid;
        $scope.occasion = $stateParams.occasion;
        $scope.locale = $stateParams.locale;
        $scope.next = function () {
            console.log($scope.survey);
            var questionList = [];
            for (var x = 0; x < $scope.survey.categories.length; x++) {
                for (var i = 0; i < $scope.survey.categories[x].questions.length; i++) {
                    questionList.push({
                        type: $scope.survey.categories[x].questions[i].type,
                        questionKey: $scope.survey.categories[x].questions[i].key,
                        answers: $scope.survey.categories[x].questions[i].answer,
                        category: $scope.survey.categories[x].questions[i].category
                    });
                }
            }
            var surveyFirst = {
                name: $scope.surveyContent.id,
                occasion: $scope.occasion,
                questionList: questionList,
                accessKey: $stateParams.id
            };
            PublicSurvey.save(surveyFirst, onSaveFinished);

        };

        var onSaveFinished = function (result) {
            console.log('save result', result);
            $state.go("survey-form.demographics", {id: result.reportId});
        };
        $scope.load = function () {
            var id = $stateParams.id;
            console.log('load called');
            $scope.survey.categories = [];
            PublicSurvey.questions({id: id}, function (result) {
                $scope.surveyContent = result;
                var category = $scope.surveyContent.questions[0].category;
                var questionList = [];
                var categories = [{
                    name: $scope.surveyContent.questions[0].category,
                    questions: []
                }];
                var catindex = 0;
                for (var i = 0; i < $scope.surveyContent.questions.length; i++) {
                    var question = $scope.surveyContent.questions[i];
                    var questionTxt = $scope._getQText(question);
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
                $scope.survey.categories = categories;
            });

        };
        $scope._getQText = function (question) {
            var textResult = '';
            for (var j = 0; j < question.text.length; j++) {
                if (question.text[j].locale == $scope.locale) {
                    textResult = question.text[j].fullText;
                }
            }
            return textResult;
        };
    });
