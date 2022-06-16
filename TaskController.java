package com.krizhanovsky.producer.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final ConcurrentHashMap<TaskType, CompletableFuture<Void>> taskMap = new ConcurrentHashMap();

    @PostMapping
    @RequestMapping("/submit")
    public void submitTask(@RequestBody TaskClass taskClass){
        CompletableFuture<Void> completableFuture = taskMap.get(taskClass.getTaskType());
        if(completableFuture!=null){
            completableFuture.thenRunAsync(taskFunction(taskClass));
        }
        else{
            taskMap.put(taskClass.getTaskType(),CompletableFuture.runAsync(taskFunction(taskClass), Executors.newSingleThreadExecutor()));
        }
    }
    private Runnable taskFunction(TaskClass taskClass){
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("Type of task(" + taskClass.getId() + "): " + taskClass.getTaskType());
                try{
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Task completed" );
            }
        };
    }
}

enum TaskType{
    YELLOW,
    GREEN,
    BLACK,
    BLUE
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class TaskClass{
  private TaskType taskType;
  private int id;
}