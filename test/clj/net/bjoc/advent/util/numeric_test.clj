(ns net.bjoc.advent.util.numeric-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.util.numeric]))

(deftest int-str?-by-examples
  (is (int-str? "0"))
  (is (int-str? "4"))
  (is (int-str? "123"))
  (is (int-str? "-123")))

(deftest prime-trial-division
  (testing "Prime recognizer: trial division algorithm"
    (doseq [n [2 3 5 7 11 13]]
      (is (TRIAL_DIVISION*prime? n)
          (str n " should be recognized as prime"))
      (is (not (TRIAL_DIVISION*not-prime? n))
          (str n " should not be recognized as not-prime")))
    (doseq [n [-1 0 1 4 6 8 9 10 12 35]]
      (is (not (TRIAL_DIVISION*prime? n))
          (str n " should not be recognized as prime"))
      (is (TRIAL_DIVISION*not-prime? n)
          (str n " should be recognized as not-prime")))))

(deftest digit-seq-sanity
  (is (= [1 2 3] (digit-seq 123)))
  (is (= [1 0 1 0] (digit-seq 1010))))

