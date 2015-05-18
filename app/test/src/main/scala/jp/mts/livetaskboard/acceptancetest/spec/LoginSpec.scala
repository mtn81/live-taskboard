package jp.mts.livetaskboard.acceptancetest.spec

import org.specs2._
import org.junit.runner._
import org.junit.runners.JUnit4
import jp.mts.livetaskboard.acceptancetest.helper.api.AuthApi
import jp.mts.livetaskboard.acceptancetest.helper.ui.LoginUi
import jp.mts.livetaskboard.acceptancetest.fixture.UserFixture
import org.specs2.runner.JUnitRunner
import org.specs2.specification.dsl.GWT
import org.specs2.specification.script.StandardDelimitedStepParsers
import jp.mts.livetaskboard.acceptancetest.helper.ui.TaskboardUi

@RunWith(classOf[JUnitRunner])
class LoginSpec extends Specification with GWT with StandardDelimitedStepParsers { def is = s2"""

  正常にログインできること
    Given ログインID {taro@test.jp}、パスワード {pass}、名前 {タスク太郎} のユーザが存在する  $g1
    When ログインID {taro@test.jp}、パスワード {pass} でログインする  $w1
    Then ログインユーザ名 {タスク太郎} が表示されていること  $t1
    
  """
    
  val authApi = new AuthApi()
  val loginUi = new LoginUi()
  val taskboardUi = new TaskboardUi()

  def g1 = step(threeStrings) { _ match { case (loginId, password, userName) =>
    authApi.registerUser(loginId, password, userName)
  }} 
  def w1 = step(twoStrings) { _ match { case (loginId, password) =>
    loginUi.login(loginId, password) 
  }} 
  def t1 = example(aString) { userName => 
    taskboardUi.displaysUserName(userName) must beTrue
  }
    
}