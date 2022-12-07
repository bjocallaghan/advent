(ns net.bjoc.advent.year-2022.day-7-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-7 :refer :all]))

(deftest size-by-examples
  (let [fs (file->fs "data/year_2022/day_7_example1.input")]
    (is (= 584 (size fs ["a" "e"])))
    (is (= 94853 (size fs ["a"])))
    (is (= 24933642 (size fs ["d"])))
    (is (= 48381165 (size fs [])))))

(deftest selective-dir-sum-by-example
  (is (= 95437 (file->selective-dir-sum "data/year_2022/day_7_example1.input"))))

(deftest best-dir-size-by-example
  (is (= 24933642 (file->best-dir-size "data/year_2022/day_7_example1.input"))))
