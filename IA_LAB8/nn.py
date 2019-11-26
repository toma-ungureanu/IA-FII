import numpy as np
import csv
import concurrent.futures


class Neural_Network(object):
    def __init__(self, num_hidden, learning_rate):
        # parameters
        self.inputSize = 7
        self.outputSize = 1
        self.hiddenSize = num_hidden
        self.learning_rate = learning_rate

        # weights
        self.W1 = np.random.rand(self.inputSize, self.hiddenSize)
        self.W2 = np.random.rand(self.hiddenSize, self.outputSize)

    def forward(self, input):
        # forward propagation through our network
        self.z = np.dot(input, self.W1)  # dot product of input (input) and first set of weights
        self.z2 = self.sigmoid(self.z)  # activation function
        self.z3 = np.dot(self.z2, self.W2)  # dot product of hidden layer (z2) and second set of weights

        # final activation function
        return self.sigmoid(self.z3)

    def sigmoid(self, s):
        # activation function
        return 1 / (1 + np.exp(-s))

    def sigmoidPrime(self, s):
        # derivative of sigmoid
        return s * (1 - s)

    def backward(self, input, output, backward):
        # backward propgate through the network
        self.o_delta = (output - backward) * self.sigmoidPrime(backward)  # applying derivative of sigmoid to error

        self.z2_error = self.o_delta.dot(
            self.W2.T)  # z2 error: how much our hidden layer weights contributed to output error
        self.z2_delta = self.z2_error * self.sigmoidPrime(self.z2)  # applying derivative of sigmoid to z2 error

        self.W1 += input.T.dot(self.z2_delta) * self.learning_rate  # adjusting first set (input --> hidden) weights
        self.W2 += self.z2.T.dot(self.o_delta) * self.learning_rate  # adjusting second set (hidden --> output) weights

    def train(self, input, output):
        self.backward(input, output, self.forward(input))

    def think(self, input):
        # passing the inputs via the neuron to get output
        print("Thought very hard about it:")
        print(self.forward(input))


def read_training_data(file):
    csv_file = open(file)
    training_set = csv.reader(csv_file, delimiter=',')
    input_all = []

    for row in training_set:
        for j in range(0, len(row)):
            if row[j].count("      ") > 0:
                input_all.append([int(x.strip(' ')) for x in row[:j]])
                break

    return (input_all)


def back_propagation(hidden_layer_neurons, generations, learning_rate):
    training_inputs = read_training_data("segments.data")
    training_outputs = np.array(([0], [1], [2], [3], [4], [5], [6], [7], [8], [9]), dtype=float)
    training_outputs1 = np.array(([0], [1], [2], [3], [4], [5], [6], [7], [8], [9]), dtype=int)

    # scale units
    training_inputs = training_inputs / np.amax(training_inputs, axis=0)
    training_outputs = np.divide(training_outputs, 10)

    NN = Neural_Network(hidden_layer_neurons, learning_rate)
    for i in range(generations):
        print("Input:")
        print(np.array(training_inputs, dtype=int))
        print("Actual Output:")
        print(training_outputs1.T)
        print("Predicted Output:")
        print(NN.forward(training_inputs))
        print("MSE:")
        print(np.mean(np.square(training_outputs - NN.forward(training_inputs)))) # mean sum squared loss
        NN.train(training_inputs, training_outputs)

    return NN


def main():
    hidden_layer_neurons = int(input("Enter the number of neurons in the hidden layer: "))
    generations = int(input("Enter the number of generations: "))
    learning_rate = float(input("Enter the learning rate: "))
    executor = concurrent.futures.ThreadPoolExecutor()
    future = executor.submit(back_propagation, hidden_layer_neurons, generations, learning_rate)
    to_predict = list(input('Enter the vector representing the 7 LEDs, no spaces required: '))
    to_predict = np.array(to_predict, dtype=float)

    trained_nn = future.result()
    trained_nn.think(to_predict)


main()
