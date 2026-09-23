package org.example.homeworks;
/*
Попробуйте реализовать собственный пул потоков.
В качестве аргументов конструктора пулу передаётся его емкость (количество рабочих потоков).
Как только пул создан, он сразу инициализирует и запускает потоки.
Внутри пула очередь задач на исполнение организуется через LinkedList.
При выполнении у пула потоков метода execute (Runnabler), указанная задача должна попасть в очередь исполнения,
и как только появится свободный поток - должна быть выполнена.
Также необходимо реализовать метод shutdown(), после выполнения которого новые задачи больше не принимаются пулом
(при попытке добавить задачу можно бросать IllegalStateException),
и все потоки для которых больше нет задач завершают свою работу.
Дополнительно можно добавить метод awaitTermination() без таймаута, работающий аналогично стандартным пулам потоков
*/
public class DemoThread {
    public static void main(String[] args) throws InterruptedException {
        final int tasksCount = 20;
        final int threadCount = 7;
        ThreadPool pool = new ThreadPool(threadCount);

        System.out.println("Всего задач: " + tasksCount);
        System.out.println("Количество потоков в пуле: " + threadCount);

        for (int i = 0; i < tasksCount; i++) {
            final int n = i;
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " -> задача №" + n);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            });
        }

        pool.shutdown();

        // новые задачи принимать нельзя
        try {
            pool.execute(() -> {
                System.out.println("Упс! Ещё одна задача прилетела :-/"); // для проверки, будет выводиться запись или нет
            });
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        pool.awaitTermination();
        System.out.println("Все задачи выполнены!");
    }
}
