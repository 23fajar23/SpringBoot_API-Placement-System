package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.TestRequest;
import com.placement.Placement.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.PLACEMENT)
public class TestController {

    private final TestService testService;

    @GetMapping(AppPath.ALL)
    public ResponseEntity<?> getAllTests(){
        return testService.getAll();
    }

    @GetMapping(AppPath.PAGE)
    public ResponseEntity<?> getAllTestsPage(
            @RequestParam(name = "placement", required = false) String placement,
            @RequestParam(name = "rolePlacement", required = false) String rolePlacement,
            @RequestParam(name = "page" , required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size" , required = false, defaultValue = "5") Integer size
    ) {
        return testService.getAllByPlacementAndRole(placement, rolePlacement, page, size);
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getTestById(@PathVariable String id){
        return testService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> createTest(@RequestBody TestRequest testRequest){
        return testService.create(testRequest);
    }

    @PutMapping
    public ResponseEntity<?> updateTest(@RequestBody TestRequest testRequest) {
        return testService.update(testRequest);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> removeTest(@PathVariable String id) {
        return testService.remove(id);
    }

}
