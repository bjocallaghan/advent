(ns net.bjoc.advent.year-2019.day-6-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2019.day-6]))

(deftest orbit-count-by-examples
  (is (= 3 (file->orbit-count "data/year_2019/day_6_example1.input" "D")))
  (is (= 7 (file->orbit-count "data/year_2019/day_6_example1.input" "L")))
  (is (= 42 (file->orbit-count "data/year_2019/day_6_example1.input"))))

(deftest common-ancestor-sanity
  (is (= 'D (common-ancestor '(COM B C D E J K YOU) '(COM B C D I SAN)))))

(deftest transfer-count-by-example
  (is (= 4 (file->transfer-count "data/year_2019/day_6_example2.input"))))
