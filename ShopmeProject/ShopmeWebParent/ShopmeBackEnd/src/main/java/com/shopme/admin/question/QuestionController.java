package com.shopme.admin.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Question;
import com.shopme.common.exception.QuestionNotFoundException;

@Controller
public class QuestionController {
   private String defaultRedirectURL = "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
   
   @Autowired
   private QuestionService questionService;
   
   @GetMapping("/questions")
   public String listFirstPage() {
	   return defaultRedirectURL;
   }
   
   @GetMapping("/questions/page/{pageNum}")
   public String listByPage(@PagingAndSortingParam(listName = "listQuestions",moduleURL = "/questions") PagingAndSortingHelper helper, 
		   @PathVariable(name = "pageNum") int pageNum) {
	   
	   questionService.listByPage(pageNum, helper);
	   return "questions/questions";
   }
   
   @GetMapping("/questions/detail/{id}")
   public String viewQuestion(@PathVariable("id") Integer questionId, Model model, RedirectAttributes ra) {
	   try {
		Question question = questionService.getQuestionById(questionId);
		model.addAttribute("question",question);
		return "questions/question_detail_modal";
	} catch (QuestionNotFoundException e) {
		// TODO Auto-generated catch block
		ra.addFlashAttribute("message",e.getMessage());
		return defaultRedirectURL;
	}
   }
   
   
   @GetMapping("/questions/edit/{id}")
   public String editQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
	   try {
		Question question = questionService.getQuestionById(id);
		model.addAttribute("question",question);
		model.addAttribute("pageTitle","Edit question with id: " + id);
		return "questions/question_form";
	} catch (QuestionNotFoundException e) {
		// TODO Auto-generated catch block
		ra.addFlashAttribute("message",e.getMessage());
		return defaultRedirectURL;
	}
	   
   }
   
   @PostMapping("/questions/save")
   public String save(Question question,RedirectAttributes ra,@AuthenticationPrincipal ShopmeUserDetails userDetails) {
	   try {
		   questionService.savedQuestionByUser(question,userDetails.getUser());
		   ra.addFlashAttribute("message","The Question ID " + question.getId() + " has been updated successfully.");
	   }catch(QuestionNotFoundException e) {
		   ra.addFlashAttribute("message", "Could not find any question with ID " + question.getId());
	   }
	   return defaultRedirectURL;
   }
   
   
   @GetMapping("/questions/{id}/approve")
   public String approveQuestion(@PathVariable("id") Integer id, RedirectAttributes ra) {
	   questionService.approve(id);
	   ra.addFlashAttribute("message", "The Question ID " + id + " has been approved.");
	   return defaultRedirectURL;
   }
   
   @GetMapping("/questions/{id}/disapprove")
   public String disapproveQuestion(@PathVariable("id") Integer id, RedirectAttributes ra) {
	   questionService.disapprove(id);
	   ra.addFlashAttribute("message", "The Question ID " + id + " has been disapproved.");
	   return defaultRedirectURL;
   }
   
   @GetMapping("/questions/{id}/delete")
   public String delete(@PathVariable("id") Integer id,RedirectAttributes ra) {
	   try {
		   questionService.delete(id);
		   ra.addFlashAttribute("message","The Question ID " + id + " has been deleted.");
	   } catch (QuestionNotFoundException e) {
		// TODO Auto-generated catch block
		   ra.addFlashAttribute("message",e.getMessage());
	   }
	   
	   return defaultRedirectURL;
   }
}
