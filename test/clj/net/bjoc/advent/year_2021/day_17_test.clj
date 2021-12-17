(ns net.bjoc.advent.year-2021.day-17-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-17]))

(deftest valid-velocity-by-examples
  (let [target (file->target "data/year_2021/day_17_example1.input")
        valid-velocity? (valid-velocity-pred target)]
    (is (valid-velocity? [7 2]))
    (is (valid-velocity? [6 3]))
    (is (valid-velocity? [9 0]))
    (is (not (valid-velocity? [17 -4])))))

(deftest velocity-max-height-by-examples
  (is (= 45 (velocity-max-height [6 9])))
  (is (= 0 (velocity-max-height [9 0]))))

(deftest find-best-by-example
  (is (= 45 (file->find-best "data/year_2021/day_17_example1.input"))))

(deftest count-valid-by-example
  (is (= 112 (file->count-valid "data/year_2021/day_17_example1.input"))))
