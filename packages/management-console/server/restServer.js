const express = require('express')
const app = express();
var cors = require('cors')

const mockData = {
    management: {
        process: [{
            processId: 'flightBooking', 
            instances: [{
                processInstanceId: 'a23e6c20-02c2-4c2b-8c5c-e988a0adf863',
                error: 'Something went wrong'
            },
            {
                processInstanceId: 'a23e6c20-02c2-4c2b-8c5c-e988a0adf864',
                error: 'some thing went wrong'
            },
            {
                processInstanceId: 'a23e6c20-02c2-4c2b-8c5c-e988a0adf865',
                error: 'some thing went wrong'
            }]
            
        },
        {
            processId: 'travels', 
            instances: [{
                processInstanceId: 'e4448857-fa0c-403b-ad69-f0a353458b9d',
                error: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim idest laborum.'
            },
            {
                processInstanceId: 'a23e6c20-02c2-4c2b-8c5c-e988a0adf867',
                error: 'some thing went wrong'
            },
            {
                processInstanceId: 'a23e6c20-02c2-4c2b-8c5c-e988a0adf868',
                error: 'some thing went wrong'
            }]
            
        }]
    }
} 

const controller = {
    showError : (req, res) => {
        console.log('called', req.params.processId, req.params.processInstanceId)
        const {process} = mockData.management;
        const processId = process.filter((data)     => {
            return data.processId === req.params.processId
        })
        const error = processId[0].instances.filter((err) => {
            return err.processInstanceId === req.params.processInstanceId
        })
        res.send(error[0].error)
    }
}

app.use(cors())
// http://localhost:8090/management/process/{processId}/instances/{processInstanceId}/error
app.get('/management/process/:processId/instances/:processInstanceId/error', controller.showError)

app.listen('8090', () =>{
    console.log('connected to port rest server 8090')
})

