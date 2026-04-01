package common.commands;

import common.MetaHashSet;
import common.stored.Route;

import java.util.Deque;

import static common.commands.History.history;

public class Help extends Command{
    public Help(MetaHashSet<Route> collection, String filename, Deque<String> history) {
        super(collection, filename, history);
    }
    @Override
    public void execute(CArgs args) {
        System.out.println("""
                help : вывести справку по доступным командам
                info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                add {element} : добавить новый элемент в коллекцию
                update id {element} : обновить значение элемента коллекции, id которого равен заданному
                remove_by_id id : удалить элемент из коллекции по его id
                clear : очистить коллекцию
                save : сохранить коллекцию в файл
                execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                exit : завершить программу (без сохранения в файл)
                add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
                remove_greater {element} : удалить из коллекции все элементы, превышающие заданный
                history : вывести последние 5 команд (без их аргументов)
                count_less_than_distance distance : вывести количество элементов, значение поля distance которых меньше заданного
                print_descending : вывести элементы коллекции в порядке убывания
                print_field_descending_distance : вывести значения поля distance всех элементов в порядке убывания""");
        history(args.group1(), this.history);
    }
}
