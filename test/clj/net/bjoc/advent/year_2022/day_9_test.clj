(ns net.bjoc.advent.year-2022.day-9-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-9 :refer :all]))

(deftest visited-count-by-example
  (is (= 13 (file->visited-count 2 "data/year_2022/day_9_example1.input")))
  (is (= 1 (file->visited-count 10 "data/year_2022/day_9_example1.input")))
  (is (= 36 (file->visited-count 10 "data/year_2022/day_9_example2.input"))))
