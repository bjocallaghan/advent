(ns net.bjoc.advent.year-2024.day-3-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2024.day-3 :refer :all]))

(deftest mul-sum-by-example
  (is (= 161 (mul-sum "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"))))

(deftest eval-op-by-example
  (is (= {:acc 13 :on? true} (eval-op {:acc 3 :on? true} {:op :mul :operands [2 5]})))
  (is (= {:acc 3 :on? false} (eval-op {:acc 3 :on? false} {:op :mul :operands [2 5]})))
  (is (= {:acc 3 :on? false} (eval-op {:acc 3 :on? true} {:op :don't})))
  (is (= {:acc 3 :on? true} (eval-op {:acc 3 :on? false} {:op :do}))))

(deftest parse-by-example
  (is (= {:op :mul :operands [2 4]}
         (first (parse "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")))))

(deftest mul-sum*-by-example
  (is (= 48 (mul-sum* "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"))))
