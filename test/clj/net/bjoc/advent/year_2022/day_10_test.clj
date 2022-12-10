(ns net.bjoc.advent.year-2022.day-10-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-10 :refer :all]))

(deftest signal-at-by-examples
  (let [instructions (file->instructions "data/year_2022/day_10_example1.input")]
    (is (= 420 (signal-at 20 instructions)))
    (is (= 1140 (signal-at 60 instructions)))
    (is (= 1800 (signal-at 100 instructions)))
    (is (= 2940 (signal-at 140 instructions)))
    (is (= 2880 (signal-at 180 instructions)))
    (is (= 3960 (signal-at 220 instructions)))))

(deftest signal-sum-by-example
  (is (= 13140 (signal-sum "data/year_2022/day_10_example1.input"))))

(deftest screen-test
  (let [instructions (file->instructions "data/year_2022/day_10_example1.input")
        {screen :screen} (reduce step starting-state instructions)
        row-1-pixels (sort (keep (fn [[x y]] (when (zero? y) x)) (keys screen)))]
    (is (= #{0 1 4 5 8 9 12 13 16 17 20 21 24 25 28 29 32 33 36 37} (set row-1-pixels)))))
