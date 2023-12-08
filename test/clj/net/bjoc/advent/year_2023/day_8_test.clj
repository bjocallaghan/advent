(ns net.bjoc.advent.year-2023.day-8-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-8 :refer :all]))

(deftest file->num-steps-by-example
  (is (= 2 (file->num-steps "data/year_2023/day_8_example1.input")))
  (is (= 6 (file->num-steps "data/year_2023/day_8_example2.input"))))

(deftest file->num-ghost-steps-by-example
  (is (= 6 (file->num-ghost-steps "data/year_2023/day_8_example3.input"))))
