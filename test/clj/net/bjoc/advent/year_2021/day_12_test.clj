(ns net.bjoc.advent.year-2021.day-12-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-12]))

(deftest num-paths-by-examples
  (is (= 10 (file->num-paths "data/year_2021/day_12_example1.input")))
  (is (= 19 (file->num-paths "data/year_2021/day_12_example2.input")))
  (is (= 226 (file->num-paths "data/year_2021/day_12_example3.input"))))

(deftest num-paths*-by-examples
  (is (= 36 (file->num-paths* "data/year_2021/day_12_example1.input")))
  (is (= 103 (file->num-paths* "data/year_2021/day_12_example2.input")))
  ;; takes too long
  ;; (is (= 3509 (file->num-paths* "data/year_2021/day_12_example3.input")))
  )
