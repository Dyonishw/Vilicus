package forms

import forms.DeleteForm.DeleteIdClass
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Request
import play.api.test.FakeRequest

class DeleteFormSpec extends PlaySpec {

  "Delete form " should {

    "should work with bindFromRequest" in {

      val call = controllers.routes.DeleteController.deleteItem()
      implicit val request: Request[_] = FakeRequest(call)
        .withFormUrlEncodedBody("deleteID" -> "1")

      val boundForm = DeleteForm.deleteForm.bindFromRequest()
      val formData = boundForm.value.get

      formData.id mustBe 1
      formData mustBe a [DeleteIdClass]

    }

    "should fail with incorrect data" in {

      val call = controllers.routes.DeleteController.deleteItem()
      implicit val request: Request[_] = FakeRequest(call)
        .withFormUrlEncodedBody("deleteID" -> "0")

      val boundForm = DeleteForm.deleteForm.bindFromRequest()
      val errorsInForm = boundForm.errors.head

      errorsInForm.key mustBe "deleteID"
      errorsInForm.message mustBe "error.min"
      boundForm.hasGlobalErrors mustBe false
      boundForm.hasErrors mustBe true

    }
  }
}
