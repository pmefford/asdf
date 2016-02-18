'use strict';

angular.module('patientfocusApp').controller('SurveyContentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SurveyContent',
        function ($scope, $stateParams, $modalInstance, entity, SurveyContent) {

            $scope.surveyContent = entity;
            $scope.load = function (id) {
                console.log('load id:' + id);
                SurveyContent.get({id: id}, function (result) {
                    $scope.surveyContent = result;
                });
            };

            var onSaveFinished = function (result) {
                console.log('on save finished', result);
                $scope.$emit('patientfocusApp:surveyContentUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                console.log('save id:' + $scope.surveyContent.id);
                if ($scope.surveyContent.id != null) {
                    SurveyContent.update($scope.surveyContent, onSaveFinished);
                } else {
                    SurveyContent.save($scope.surveyContent, onSaveFinished);
                }

            };

            $scope.clear = function () {
                console.log('clear');
                $modalInstance.dismiss('cancel');
            };

            $scope.addTranslation = function (question) {
                console.log('addTranslation');
                var text = {'fullText': '', 'locale': 'en_US'};
                question.text.push(text);
            };

            $scope.addQuestion = function () {
                console.log('addQuestion');
                var question = {
                    'key': 'q' + $scope.surveyContent.questions.length,
                    'text': [
                        {'fullText': '', 'locale': 'en_US'}
                    ]
                };
                $scope.surveyContent.questions.push(question);
            };

            $scope.changeTextLocale = function (text, locale) {
                console.log('questionTxtLocale', text, locale);
                text.locale = locale;
            };
            $scope.chooseType = function (question, type) {
                console.log('chooseType', question, type);
                question.type = type;
                if (question.type == 'options') {
                    question.options = [{text: ''}];
                }
            };

            $scope.clearTranslation = function (question, text) {
                for (var i = 0; i < question.text.length; i++) {
                    console.log(question.text[i], text);
                    if (question.text[i].locale === text.locale && question.text.length > 1) {
                        question.text.splice(i, 1);
                        break;
                    }
                }
            };
            $scope.removeOption = function (question, index) {
                question.options.splice(index, 1);
            };
            $scope.addOption = function (question) {
                question.options.push({text: ''});
            }

        }
    ]
);
