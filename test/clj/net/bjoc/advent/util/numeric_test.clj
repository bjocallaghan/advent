(ns net.bjoc.advent.util.numeric-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.util.numeric]))

(deftest int-str?-by-examples
  (is (int-str? "0"))
  (is (int-str? "4"))
  (is (int-str? "123"))
  (is (int-str? "-123")))

(deftest exp-by-examples
  (is (= 9 (exp 3 2)))
  (is (= 8 (exp 2 3))))

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

(deftest prime-factorization-by-examples
  (is (= {2 1} (prime-factorization 2)))
  (is (= {3 1} (prime-factorization 3)))
  (is (= {2 1 17 1} (prime-factorization 34)))
  (is (= {2 3 3 2} (prime-factorization 72)))
  (is (= {3 1 23 1} (prime-factorization 69)))
  (is (= {79 1} (prime-factorization 79)))
  (is (= {59 2 281 1} (prime-factorization 978161))))

(deftest lcm-by-examples
  (is (= 6 (lcm 2 3)))
  (is (= 144 (lcm 72 16)))
  (is (= 13334102464297 (lcm 18827 16579 13207 17141 14893 22199))))
