(ns net.bjoc.advent.year-2021.day-11-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-11]))

(deftest post-steps-flash-count-by-examples
  (is (= 204 (file->post-steps-flash-count "data/year_2021/day_11_example1.input" 10)))
  (is (= 1656 (file->post-steps-flash-count "data/year_2021/day_11_example1.input"))))

(deftest flash-sync-by-example
  (is (= 195 (file->flash-sync "data/year_2021/day_11_example1.input"))))
