package passphrases

import org.scalatest.FunSuite

class PassphrasesSuite extends FunSuite with Solver {

  test("without duplicates") {
    assert(withoutDuplicates("aa bb cc dd ee"))
    assert(!withoutDuplicates("aa bb cc dd aa"))
    assert(withoutDuplicates("aa bb cc dd aaa"))
  }

  test("without anagrams") {
    assert(withoutAnagrams("abcde fghij"))
    assert(!withoutAnagrams("abcde xyz ecdab"))
    assert(withoutAnagrams("a ab abc abd abf abj"))
    assert(withoutAnagrams("iiii oiii ooii oooi oooo"))
    assert(!withoutAnagrams("oiii ioii iioi iiio"))
  }
}
