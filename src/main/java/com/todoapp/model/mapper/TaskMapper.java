package com.todoapp.model.mapper;

import com.todoapp.model.dto.TaskDTO;
import com.todoapp.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper extends DtoMapper<Task, TaskDTO> {

    @Mapping(target = "status", expression = "java(task.getStatus().getName())")
    @Override
    TaskDTO toDto(Task task);

    @Mapping(target = "status", expression = "java(com.todoapp.model.entity.TaskStatus.of(taskDTO.getStatus()))")
    @Override
    Task toEntity(TaskDTO taskDTO);

}
