(ns net.bjoc.advent.year-2016.day-9-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2016.day-9 :refer :all]))

(deftest primitives-sanity
  (is (= {:length 12 :multiplier 34} (parse-expansion "(12x34)")))
  (is (= ["abc" "def"] (map as-str (split-after #{\c} "abcdef")))))

(deftest expand-by-example
  (is (= "ADVENT" (as-str (expand "ADVENT"))))
  (is (= "ABBBBBC" (as-str (expand "A(1x5)BC"))))
  (is (= "XYZXYZXYZ" (as-str (expand "(3x3)XYZ"))))
  (is (= "ABCBCDEFEFG" (as-str (expand "A(2x2)BCD(2x2)EFG"))))
  (is (= "(1x3)A" (as-str (expand "(6x1)(1x3)A"))))
  (is (= "X(3x3)ABC(3x3)ABCY" (as-str (expand "X(8x2)(3x3)ABCY")))))

(deftest count-expansion-2-by-example
  (is (= 9 (count-expansion-2 "(3x3)XYZ")))
  (is (= 20 (count-expansion-2 "X(8x2)(3x3)ABCY")))
  (is (= 241920 (count-expansion-2 "(27x12)(20x12)(13x14)(7x10)(1x12)A")))
  (is (= 445 (count-expansion-2 "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"))))
