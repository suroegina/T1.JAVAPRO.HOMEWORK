package org.example.homeworks;

import java.util.LinkedList;

public class ThreadPool {
    // Очередь задач
    private final LinkedList<Runnable> tasks = new LinkedList<>();

    // Рабочие потоки пула
    private final Thread[] workers;

    // Флаг, выставляется в shutdown(). После него новые задачи не принимаются.
    private volatile boolean isShutdown = false;

    public ThreadPool(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Рабочих потоков должно быть > 0");
        }
        this.workers = new Thread[threadCount];

        // Сразу создаём и стартуем рабочие потоки
        for (int i = 0; i < threadCount; i++) {
            workers[i] = new Thread(this::workerLoop, "Рабочий поток №" + i);
            workers[i].start();
        }
    }

    // Добавление задачи в очередь
    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("Нет задач");
        }
        synchronized (tasks) {
            if (isShutdown) {
                throw new IllegalStateException("Новые задачи больше не принимаются пулом!");
            }
            tasks.addLast(task);
            // вызываем один из ожидающих рабочих потоков
            tasks.notify();
        }
    }

    // Останавливает приём новых задач. Потоки, для которых больше нет работы, завершаются; начатые задачи дорабатываются.
    public void shutdown() {
        synchronized (tasks) {
            if (isShutdown) {
                return;
            }
            isShutdown = true;
            // разбудить всех, чтобы каждый проверил флаг и завершился
            tasks.notifyAll();
        }
    }

    // Ждём завершения всех рабочих потоков (без таймаута).
    public void awaitTermination() throws InterruptedException {
        for (Thread worker : workers) {
            worker.join();
        }
    }

    // Основной цикл рабочего потока.
    private void workerLoop() {
        while (true) {
            Runnable task;
            synchronized (tasks) {
                // Ждём задачу или shutdown
                while (tasks.isEmpty() && !isShutdown) {
                    try {
                        tasks.wait();
                    } catch (InterruptedException e) {
                        // завершаем поток при прерывании
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                // Если shutdown и очередь пуста — этому потоку больше нечего делать
                if (tasks.isEmpty()) {
                    return;
                }
                task = tasks.removeFirst();
            }

            // Выполняем задачу вне synchronized, чтобы не блокировать очередь
            try {
                task.run();
            } catch (RuntimeException e) {
                System.err.println("Задача с ошибкой: " + e);
            }
        }
    }
}

