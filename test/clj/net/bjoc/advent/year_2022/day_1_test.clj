(ns net.bjoc.advent.year-2022.day-1-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2022.day-1]))

(deftest best-score-by-example
  (is (= 24000 (file->best-score "data/year_2022/day_1_example1.input"))))

(deftest best-3-scores-by-example
  (is (= 45000 (file->best-3-scores "data/year_2022/day_1_example1.input"))))
