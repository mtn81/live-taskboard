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
class RidirectToLoginSpec extends Specification with GWT with StandardDelimitedStepParsers { def is = s2"""

  ログインしていない場合、ログインにリダイレクトされること
    Given ログインID {taro2@RidirectToLoginSpec}、パスワード {pass} のユーザが存在する  $g1
    Given ログインID {taro2@RidirectToLoginSpec}、パスワード {pass} でログインする  $g2
    When ログアウトする  $w1
    When タスクボードを利用しようとする  $w2
    Then ログインが求められる  $t1
    
  """
    
  val usersApi = new UsersApi()
  val loginUi = new LoginUi()
  val taskboardUi = new TaskboardUi()

  def g1 = step(twoStrings) { _ match { case (loginId, password) =>
    usersApi.registerUser(loginId, password) 
  }} 
  def g2 = step(twoStrings) { _ match { case (loginId, password) =>
    loginUi.login(loginId, password) 
  }} 
  def w1 = step { taskboardUi.logout() } 
  def w2 = step { taskboardUi.intendToUse() } 
  def t1 = { loginUi.promptLogin() must beTrue }
    
}