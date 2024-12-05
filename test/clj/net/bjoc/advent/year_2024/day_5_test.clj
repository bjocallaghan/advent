(ns net.bjoc.advent.year-2024.day-5-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2024.day-5 :refer :all]))

(deftest line->rule-by-example
  (is (= true ((line->rule "47|53") [47 53])))
  (is (= true ((line->rule "47|53") [75 47 61 53 29])))
  (is (= false ((line->rule "47|53") [75 61 53 47 29])))
  (is (= true ((line->rule "47|53") [])))
  (is (= true ((line->rule "47|53") [53])))
  (is (= true ((line->rule "47|53") [47])))

(deftest middle-by-example
  (is (= 2 (middle [1 2 3])))
  (is (= 3 (middle [1 2 3 4 5]))))

(deftest file->middle-sum-by-example
  (is (= 143 (file->middle-sum "data/year_2024/day_5_example.input"))))

(deftest file->middle-sum*-by-example
  (is (= 123 (file->middle-sum* "data/year_2024/day_5_example.input"))))
