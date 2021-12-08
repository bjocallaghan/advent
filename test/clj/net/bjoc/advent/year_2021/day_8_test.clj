(ns net.bjoc.advent.year-2021.day-8-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-8]))

(deftest count-1478-by-example
  (is (= 26 (file->count-1478 "data/year_2021/day_8_example2.input"))))

(deftest notes->n-by-example
  (let [notes (->> "data/year_2021/day_8_example1.input"
                   file->all-notes
                   first)]
    (is (= 5353 (notes->n notes)))))

(deftest sum-by-example
  (is (= 61229 (file->sum "data/year_2021/day_8_example2.input"))))
