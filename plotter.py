import matplotlib.pyplot as plt


def dim_50_pop_100():
    single_fits = [7.18, 7.23, 7.22, 7.25, 7.22]
    single_times_ms = [21, 26, 34, 41, 49]

    threaded_fits = [7.23, 7.18, 7.17, 7.05, 7.22]
    threaded_times_ms = [23, 25, 24, 25, 26]

    islands_fits = [6.86, 6.83, 6.92, 6.89, 6.94]
    islands_times_ms = [232, 228, 213, 226, 216]

    plot_fitness_complexity(single_fits, threaded_fits, islands_fits)
    plot_duration_complexity(single_times_ms, threaded_times_ms, islands_times_ms)


def plot_fitness_complexity(
        single_fits,
        threaded_fits,
        islands_fits,
        single_algo='Single-thread',
        threaded_algo='Master-slave',
        islands_algo='Islands',
        single_color='blue',
        threaded_color='green',
        islands_color='yellow'
):
    plot(single_fits, single_color, single_algo)
    plot(threaded_fits, threaded_color, threaded_algo)
    plot(islands_fits, islands_color, islands_algo)

    plt.legend(loc='lower left')
    plt.title('Fitness-Complexity dependence')
    plt.xlabel('Complexity')
    plt.xticks(range(1, 6))
    plt.ylabel('Fitness')
    plt.show()

def plot_duration_complexity(
        single_fits,
        threaded_fits,
        islands_fits,
        single_algo='Single-thread',
        threaded_algo='Master-slave',
        islands_algo='Islands',
        single_color='blue',
        threaded_color='green',
        islands_color='yellow'
):
    plot(single_fits, single_color, single_algo)
    plot(threaded_fits, threaded_color, threaded_algo)
    plot(islands_fits, islands_color, islands_algo)

    plt.legend(loc='lower left')
    plt.title('Duration(ms)-Complexity dependence')
    plt.xlabel('Complexity')
    plt.xticks(range(1, 6))
    plt.ylabel('Duration (ms)')
    plt.show()


def main():
    dim_50_pop_100()


def plot(
        values,
        color,
        label,
):
    complexities = [1, 2, 3, 4, 5]
    plt.plot(complexities, values, color=color, label=label)


if __name__ == '__main__':
    main()
