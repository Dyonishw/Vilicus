package forms

import forms.UpdateForm.UpdateClass
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Request
import play.api.test.FakeRequest

class UpdateFormSpec extends PlaySpec {

  "Update Form" should {

    "should work with bindFromRequest" in {

      val call = controllers.routes.UpdateController.updateItem()
      implicit val request: Request[_] = FakeRequest(call)
        .withFormUrlEncodedBody("updateID" -> "1", "quantity" -> "100")
      val boundForm = UpdateForm.updateForm.bindFromRequest()
      val formData = boundForm.value.get

      formData.id mustBe 1
      formData.quantity mustBe 100
      formData mustBe a [UpdateClass]

    }

    "should fail with incorrect data" in {

      val call = controllers.routes.UpdateController.updateItem()
      implicit val request: Request[_] = FakeRequest(call)
        .withFormUrlEncodedBody("updateID" -> "1", "quantity" -> "-10")
      val boundForm = UpdateForm.updateForm.bindFromRequest()
      val errorsInForm = boundForm.errors.head

      errorsInForm.key mustBe "quantity"
      errorsInForm.message mustBe "error.min"
      boundForm.hasGlobalErrors mustBe false
      boundForm.hasErrors mustBe true

    }
  }
}
