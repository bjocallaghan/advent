(ns net.bjoc.advent.year-2022.day-8-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.year-2022.day-8 :refer :all]))

(deftest visible-tree-count-by-example
  (is (= 21 (file->visible-tree-count "data/year_2022/day_8_example1.input"))))

(deftest scenic-score-by-examples
  (let [filename "data/year_2022/day_8_example1.input"
        matrix (-> filename
                   slurp
                   mtx/from-string)]
    (is (= 4 (scenic-score matrix [2 1])))
    (is (= 8 (scenic-score matrix [2 3])))))

(deftest best-scenic-score-by-example
  (is (= 8 (file->best-scenic-score "data/year_2022/day_8_example1.input"))))
