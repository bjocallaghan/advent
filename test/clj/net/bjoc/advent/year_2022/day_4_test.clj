(ns net.bjoc.advent.year-2022.day-4-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-4 :refer :all]))

(deftest line->sets-by-example
  (is (= [#{2 3 4} #{6 7 8}] (line->sets "2-4,6-8")))
  (is (= [#{2 3} #{4 5}] (line->sets "2-3,4-5"))))

(deftest redundant-count-by-example
  (is (= 2 (file->redundant-count "data/year_2022/day_4_example1.input"))))

(deftest overlap-count-by-example
  (is (= 4 (file->overlap-count "data/year_2022/day_4_example1.input"))))
