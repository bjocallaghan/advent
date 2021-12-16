(ns net.bjoc.advent.year-2021.day-16-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-16]))

(deftest version-sum-by-examples
  (is (= 16 (version-sum (->> "8A004A801A8002F478" str->bits packet))))
  (is (= 12 (version-sum (->> "620080001611562C8802118E34" str->bits packet))))
  (is (= 23 (version-sum (->> "C0015000016115A2E0802F182340" str->bits packet))))
  (is (= 31 (version-sum (->> "A0016C880162017C3686B18A3D4780" str->bits packet)))))

(deftest eval-packet-by-examples
  (is (= 3 (eval-packet (->> "C200B40A82" str->bits packet))))
  (is (= 54 (eval-packet (->> "04005AC33890" str->bits packet))))
  (is (= 7 (eval-packet (->> "880086C3E88112" str->bits packet))))
  (is (= 9 (eval-packet (->> "CE00C43D881120" str->bits packet))))
  (is (= 1 (eval-packet (->> "D8005AC2A8F0" str->bits packet))))
  (is (= 0 (eval-packet (->> "F600BC2D8F" str->bits packet))))
  (is (= 0 (eval-packet (->> "9C005AC2F8F0" str->bits packet))))
  (is (= 1 (eval-packet (->> "9C0141080250320F1802104A08" str->bits packet)))))
