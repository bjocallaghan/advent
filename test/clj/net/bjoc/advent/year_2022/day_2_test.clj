(ns net.bjoc.advent.year-2022.day-2-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2022.day-2]))

(deftest score-by-example
  (is (= 15 (file->score "data/year_2022/day_2_example1.input"))))

(deftest score-2-by-example
  (is (= 12 (file->score-2 "data/year_2022/day_2_example1.input"))))
