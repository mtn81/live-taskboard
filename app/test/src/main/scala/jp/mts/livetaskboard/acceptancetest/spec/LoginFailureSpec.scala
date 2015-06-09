package jp.mts.livetaskboard.acceptancetest.spec

import org.specs2._
import org.junit.runner._
import org.junit.runners.JUnit4
import jp.mts.livetaskboard.acceptancetest.helper.ui.LoginUi
import jp.mts.livetaskboard.acceptancetest.fixture.UserFixture
import org.specs2.runner.JUnitRunner
import org.specs2.specification.dsl.GWT
import org.specs2.specification.script.StandardDelimitedStepParsers
import jp.mts.livetaskboard.acceptancetest.helper.ui.TaskboardUi
import jp.mts.livetaskboard.acceptancetest.helper.api.UsersApi

@RunWith(classOf[JUnitRunner])
class LoginFailureSpec extends Specification with GWT with StandardDelimitedStepParsers { def is = s2"""

  パスワードを間違えるとエラーになるできること
    Given ログインID {taro@LoginFailureSpec}、パスワード {pass} のユーザが存在する  $g1
    When ログインID {taro@LoginFailureSpec}、パスワード {failurepass} でログインする  $w1
    Then エラー　{認証に失敗しました} が表示されていること  $t1
    
  """
    
  val usersApi = new UsersApi()
  val loginUi = new LoginUi()
  val taskboardUi = new TaskboardUi()

  def g1 = step(twoStrings) { _ match { case (loginId, password) =>
    usersApi.registerUser(loginId, password)
  }} 
  def w1 = step(twoStrings) { _ match { case (loginId, password) =>
    loginUi.login(loginId, password) 
  }} 
  def t1 = example(aString) { errorMessage => 
    loginUi.displaysError(errorMessage) must beTrue
  }
    
}