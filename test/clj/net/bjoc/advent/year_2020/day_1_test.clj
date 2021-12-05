(ns net.bjoc.advent.year-2020.day-1-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2020.day-1]))

(deftest answer-by-example
  (is (= 514579 (file->answer  "data/year_2020/day_1_example1.input"))))

(deftest answer-by-example*
  (is (= 241861950 (file->answer* "data/year_2020/day_1_example1.input"))))
